package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import cccp.model.User;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.OnlineInventoryDAO;
import cccp.model.dao.OnlineInventoryDAOInterface;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.OnlineOrderService;
import cccp.service.OnlineOrderServiceInterface;
import cccp.service.ProductService;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;

@WebServlet("/OnlineOrderServlet")
public class OnlineOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OnlineOrderServiceInterface orderService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        BatchDAOInterface batchDAO = new BatchDAO();
        OnlineInventoryDAOInterface onlineDAO = new OnlineInventoryDAO();
        ProductDAOInterface productDAO = new ProductDAO();
        orderService = new OnlineOrderService(batchDAO, onlineDAO);
        productService = new ProductService(productDAO, batchDAO);
    }

    public OnlineOrderServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
        request.getRequestDispatcher("onlineStockRestock.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (user == null) {
            out.println("{\"success\": false, \"message\": \"Please login first\"}");
            return;
        }

        String productId = request.getParameter("productId");
        String quantityStr = request.getParameter("quantity");

        if (productId == null || productId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"success\": false, \"message\": \"Product ID is required\"}");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"success\": false, \"message\": \"Quantity must be greater than zero\"}");
                return;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"success\": false, \"message\": \"Invalid quantity format\"}");
            return;
        }

        BatchSelectionStrategy strategy = new ExpiryBasedSelectionStrategy(new BatchDAO());
        try {
            orderService.allocateStockForOnlineOrder(productId, quantity, new java.util.Date(), strategy);
            orderService.addRestockListener(productService);
            out.println("{\"success\": true, \"message\": \"Online inventories restocked successfully\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"success\": false, \"message\": \"Error restocking online inventories: " + e.getMessage() + "\"}");
        }
    }
}