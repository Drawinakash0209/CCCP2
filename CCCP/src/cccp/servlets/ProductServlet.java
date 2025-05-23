package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import cccp.model.Product;
import cccp.model.User;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.ProductDAO;
import cccp.service.ProductService;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductService productService;

    public ProductServlet() {
        super();
        ProductDAO productDAO = new ProductDAO();
        BatchDAO batchDAO = new BatchDAO();
        this.productService = new ProductService(productDAO, batchDAO);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }

        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (user == null) {
            out.write("{\"success\": false, \"message\": \"Please login first\"}");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                int categoryId = Integer.parseInt(request.getParameter("category_id"));
                int reorderLevel = Integer.parseInt(request.getParameter("reorder_level"));

                Product product = new Product.Builder()
                        .setId(id)
                        .setName(name)
                        .setPrice(price)
                        .setCategoryId(categoryId)
                        .setReorderLevel(reorderLevel)
                        .build();
                productService.addProduct(product);
                out.write("{\"success\": true, \"message\": \"Product added successfully\"}");
            } else if ("update".equals(action)) {
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                double price = Double.parseDouble(request.getParameter("price"));
                int categoryId = Integer.parseInt(request.getParameter("category_id"));
                int reorderLevel = Integer.parseInt(request.getParameter("reorder_level"));

                Product product = new Product.Builder()
                        .setId(id)
                        .setName(name)
                        .setPrice(price)
                        .setCategoryId(categoryId)
                        .setReorderLevel(reorderLevel)
                        .build();
                productService.updateProduct(product);
                out.println("{\"success\": true, \"message\": \"Product updated successfully\"}");
            } else if ("delete".equals(action)) {
                String id = request.getParameter("id");
                productService.deleteProduct(id);
                out.println("{\"success\": true, \"message\": \"Product deleted successfully\"}");
            } else {
                doGet(request, response);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }
}