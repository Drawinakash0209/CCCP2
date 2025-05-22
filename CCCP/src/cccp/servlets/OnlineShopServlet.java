package cccp.servlets;

import cccp.model.Bill;
import cccp.model.Product;
import cccp.model.User;
import cccp.model.dao.*;
import cccp.queue.CustomerCheckoutProcessor;
import cccp.queue.OnlineCheckoutRequest;
import cccp.service.BillingService;
import cccp.service.BillingServiceInterface;
import cccp.service.OnlineOrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@WebServlet("/onlineShop")
public class OnlineShopServlet extends HttpServlet {
    private static final ProductDAOInterface productDAO = new ProductDAO();
    private static final BatchDAOInterface batchDAO = new BatchDAO();
    private static final BillDAOInterface billDAO = new BillDAO();
    private static final SaleDAOInterface saleDAO = new SaleDAO();
    private static final OnlineInventoryDAOInterface onlineDAO = new OnlineInventoryDAO();
    private static final DeliveryDetailsDAOInterface deliveryDetailsDAO = new DeliveryDetailsDAO();
    private static final OnlineOrderService onlineOrderService = new OnlineOrderService(batchDAO, onlineDAO);
    private static final BillingServiceInterface billingService = new BillingService(billDAO, productDAO, onlineOrderService, saleDAO);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "viewProducts";
        }

        try {
            switch (action) {
                case "viewProducts":
                    showProductCatalog(request, response);
                    break;
                case "viewCart":
                    showCart(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            System.err.println("Error in OnlineShopServlet (GET): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action");
            return;
        }

        try {
            switch (action) {
                case "addToCart":
                    addToCart(request, response);
                    break;
                case "updateCart":
                    updateCart(request, response);
                    break;
                case "checkout":
                    initiateCheckout(request, response);
                    break;
                case "submitDeliveryDetails":
                    processDeliveryDetails(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            System.err.println("Error in OnlineShopServlet (POST): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void showProductCatalog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("product_catalog.jsp").forward(request, response);
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        Map<Product, Integer> detailedCart = new HashMap<>();
        double total = 0.0;
        if (!cart.isEmpty()) {
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                Product product = productDAO.getProductById(entry.getKey());
                if (product != null) {
                    detailedCart.put(product, entry.getValue());
                    total += product.getPrice() * entry.getValue();
                }
            }
        }

        request.setAttribute("detailedCart", detailedCart);
        request.setAttribute("cartTotal", total);
        request.getRequestDispatcher("cart_view.jsp").forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        try {
            String productId = request.getParameter("productId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                quantity = 1;
            }

            Product product = productDAO.getProductById(productId);
            if (product == null) {
                throw new ServletException("Product not found!");
            }

            cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
            session.setAttribute("message", product.getName() + " added to cart.");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid product ID or quantity.");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error adding item to cart: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/onlineShop?action=viewProducts");
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");

        if (cart == null) {
            response.sendRedirect(request.getContextPath() + "/onlineShop?action=viewCart");
            return;
        }

        try {
            String productId = request.getParameter("productId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (cart.containsKey(productId)) {
                if (quantity > 0) {
                    cart.put(productId, quantity);
                } else {
                    cart.remove(productId);
                }
                session.setAttribute("message", "Cart updated.");
            } else {
                session.setAttribute("errorMessage", "Item not found in cart for update.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid product ID or quantity for update.");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error updating cart: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/onlineShop?action=viewCart");
    }

    private void initiateCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Session expired. Please login again.");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            request.setAttribute("errorMessage", "Your cart is empty.");
            request.getRequestDispatcher("cart_view.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("checkout_details.jsp").forward(request, response);
    }

    private void processDeliveryDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Map<String, Integer> cart = (Map<String, Integer>) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Session expired. Please login again.");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            request.setAttribute("errorMessage", "Your cart is empty.");
            request.getRequestDispatcher("cart_view.jsp").forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phone");
        String deliveryAddress = request.getParameter("address");

        if (name == null || name.trim().isEmpty() || phoneNumber == null || phoneNumber.trim().isEmpty() || deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please provide all required delivery details.");
            request.getRequestDispatcher("checkout_details.jsp").forward(request, response);
            return;
        }

        Map<Product, Integer> itemsToPurchase = new HashMap<>();
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            Product product = productDAO.getProductById(entry.getKey());
            if (product != null && entry.getValue() > 0) {
                itemsToPurchase.put(product, entry.getValue());
            }
        }

        if (itemsToPurchase.isEmpty()) {
            request.setAttribute("errorMessage", "No valid items found in cart to purchase.");
            request.getRequestDispatcher("cart_view.jsp").forward(request, response);
            return;
        }

        OnlineCheckoutRequest checkoutRequest = new OnlineCheckoutRequest(billingService, deliveryDetailsDAO, onlineOrderService,
                                                                          itemsToPurchase, user, name, phoneNumber, deliveryAddress);
        CustomerCheckoutProcessor.addCheckoutRequest(checkoutRequest);

        try {
            CustomerCheckoutProcessor.CheckoutResult result = checkoutRequest.getResultFuture().get(30, TimeUnit.SECONDS);
            if ("success".equals(result.status)) {
                session.removeAttribute("cart");
                request.setAttribute("bill", (Bill) result.message);
                request.setAttribute("message", "Order placed successfully!");
                request.getRequestDispatcher("bill_display.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Checkout failed: " + result.message);
                request.getRequestDispatcher("checkout_details.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Checkout failed: " + e.getMessage());
            request.getRequestDispatcher("checkout_details.jsp").forward(request, response);
        }
    }
}