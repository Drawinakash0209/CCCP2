package cccp.servlets;

import cccp.model.Category;
import cccp.model.User;
import cccp.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryService categoryService;

    public CategoryServlet() {
        super();
        this.categoryService = new CategoryService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }

        try {
            List<Category> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("category.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("message", "Error: " + e.getMessage());
            request.getRequestDispatcher("category.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("Action: " + action); // Debugging line
        if (action == null) {
            request.setAttribute("message", "Error: Action parameter is missing.");
            doGet(request, response);
            return;
        }

        try {
            if ("create".equals(action)) {
                String categoryName = request.getParameter("categoryName");
                int newId = categoryService.createCategory(categoryName);
                request.setAttribute("message", "Category added successfully! New ID: " + newId);
            } else if ("update".equals(action)) {
                String idStr = request.getParameter("id");
                String categoryName = request.getParameter("categoryName");
                if (idStr == null || idStr.trim().isEmpty() || categoryName == null || categoryName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Category ID and name are required.");
                }
                int id = Integer.parseInt(idStr);
                categoryService.updateCategory(id, categoryName);
                request.setAttribute("message", "Category updated successfully!");
            } else if ("delete".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("Category ID is required for deletion.");
                }
                int id = Integer.parseInt(idStr);
                categoryService.deleteCategory(id);
                request.setAttribute("message", "Category deleted successfully!");
            } else {
                throw new IllegalArgumentException("Invalid action: " + action);
            }
        } catch (Exception e) {
            request.setAttribute("message", "Error: " + e.getMessage());
            // Re-throw to ensure CommandProcessor logs the exception
        }
        doGet(request, response);
    }
}