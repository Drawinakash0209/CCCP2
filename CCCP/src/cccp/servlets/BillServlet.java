package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cccp.PercentageDiscount;
import cccp.model.Bill;
import cccp.model.User;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.BillDAO;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAO;
import cccp.model.dao.SaleDAOInterface;
import cccp.model.dao.ShelfDAO;
import cccp.model.dao.ShelfDAOInterface;
import cccp.service.BillingService;
import cccp.service.BillingServiceInterface;
import cccp.service.ShelfService;
import cccp.service.ShelfServiceInterface;

@WebServlet("/BillServlet")
public class BillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ShelfServiceInterface shelfService;
    private BillingServiceInterface billingService;

    @Override
    public void init() throws ServletException {
        BatchDAOInterface batchDAO = new BatchDAO();
        ShelfDAOInterface shelfDAO = new ShelfDAO();
        BillDAOInterface billDAO = new BillDAO();
        ProductDAOInterface productDAO = new ProductDAO();
        SaleDAOInterface saleDAO = new SaleDAO();
        shelfService = new ShelfService(batchDAO, shelfDAO);
        billingService = new BillingService(billDAO, productDAO, shelfService, saleDAO);
    }

    public BillServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
        request.getRequestDispatcher("Bill.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
        List<Bill.BillItem> billItems = new ArrayList<>();
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        if (productIds != null && quantities != null && productIds.length == quantities.length) {
            for (int i = 0; i < productIds.length; i++) {
                String productId = productIds[i];
                if (productId == null || productId.trim().isEmpty()) continue;

                try {
                    int quantity = Integer.parseInt(quantities[i]);
                    if (quantity <= 0) {
                        request.setAttribute("error", "Quantity must be greater than zero for product: " + productId);
                        request.getRequestDispatcher("Bill.jsp").forward(request, response);
                        return;
                    }
                    Bill.BillItem billItem = billingService.createBillItem(productId, quantity);
                    billItems.add(billItem);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid quantity format for product: " + productId);
                    request.getRequestDispatcher("Bill.jsp").forward(request, response);
                    return;
                } catch (Exception e) {
                    request.setAttribute("error", "Error processing product: " + productId + " - " + e.getMessage());
                    request.getRequestDispatcher("Bill.jsp").forward(request, response);
                    return;
                }
            }
        }

        if (billItems.isEmpty()) {
            request.setAttribute("error", "No valid products selected.");
            request.getRequestDispatcher("Bill.jsp").forward(request, response);
            return;
        }

        String discountRateStr = request.getParameter("discountRate");
        String cashTenderedStr = request.getParameter("cashTendered");
        double discountRate;
        double cashTendered;

        try {
            discountRate = discountRateStr != null && !discountRateStr.isEmpty() 
                           ? Double.parseDouble(discountRateStr) : 0.0;
            cashTendered = cashTenderedStr != null && !cashTenderedStr.isEmpty() 
                           ? Double.parseDouble(cashTenderedStr) : 0.0;

            if (discountRate < 0 || discountRate > 100) {
                request.setAttribute("error", "Discount rate must be between 0 and 100.");
                request.getRequestDispatcher("Bill.jsp").forward(request, response);
                return;
            }
            if (cashTendered < 0) {
                request.setAttribute("error", "Cash tendered cannot be negative.");
                request.getRequestDispatcher("Bill.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input for discount rate or cash tendered.");
            request.getRequestDispatcher("Bill.jsp").forward(request, response);
            return;
        }

        billingService.setDiscount(new PercentageDiscount(discountRate));
        Bill bill = billingService.createBill(billItems, cashTendered);
        request.setAttribute("bill", bill);
        request.setAttribute("success", "Bill created successfully");
        request.getRequestDispatcher("Bill.jsp").forward(request, response);
    }
}