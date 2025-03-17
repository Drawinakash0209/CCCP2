package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import cccp.model.Category;
import cccp.model.dao.CategoryDAO;

/**
 * Servlet implementation class CategoryServlet
 */
@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryDAO categoryDAO = new CategoryDAO(); 

    public CategoryServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categories = categoryDAO.viewAllItemsGUI(); // Retrieve categories from database
        System.out.println("Categories: " + categories); // Print categories to console
        request.setAttribute("categories", categories); // Set the categories as request attribute
        request.getRequestDispatcher("category.jsp").forward(request, response); // Forward to JSP
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            String categoryName = request.getParameter("categoryName");
            Category category = new Category(0, categoryName);
            int result = categoryDAO.addItem(category);
            if (result == 1) {
                response.sendRedirect("CategoryServlet");
            } else {
                PrintWriter out = response.getWriter();
                out.println("Failed to add category");
            }
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String categoryName = request.getParameter("categoryName");
            Category category = new Category(id, categoryName);
            categoryDAO.updateItem(category);
            response.sendRedirect("CategoryServlet");
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            categoryDAO.removeItem(id);
            response.sendRedirect("CategoryServlet");
        } else {
            doGet(request, response); // Delegate other POST requests to GET request
        }
    }
}