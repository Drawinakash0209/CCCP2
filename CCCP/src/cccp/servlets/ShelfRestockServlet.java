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
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.ShelfDAO;
import cccp.model.dao.ShelfDAOInterface;
import cccp.service.ProductService;
import cccp.service.ShelfService;
import cccp.service.ShelfServiceInterface;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;

@WebServlet("/ShelfRestockServlet")
public class ShelfRestockServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ShelfServiceInterface shelfService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        BatchDAOInterface batchDAO = new BatchDAO();
        ShelfDAOInterface shelfDAO = new ShelfDAO();
        ProductDAOInterface productDAO = new ProductDAO();
        shelfService = new ShelfService(batchDAO, shelfDAO);
        productService = new ProductService(productDAO, batchDAO);
    }

    public ShelfRestockServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
        request.getRequestDispatcher("shelfRestock.jsp").forward(request, response);
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
            shelfService.restockShelf(productId, quantity, new java.util.Date(), strategy);
            shelfService.addRestockListener(productService);
            out.println("{\"success\": true, \"message\": \"Shelf restocked successfully\"}");
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("{\"success\": false, \"message\": \"Unexpected error: " + e.getMessage() + "\"}");
        }
    }
}