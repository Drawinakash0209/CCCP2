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
import cccp.service.categoryService;


@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {


    private final categoryService categoryService = new categoryService(); // Using service layer

    public CategoryServlet() {
        super();
    }
//test
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categories = categoryService.getAllCategories(); // Delegate to service layer
        request.setAttribute("categories", categories); // Set the categories as request attribute
        request.getRequestDispatcher("category.jsp").forward(request, response); // Forward to JSP
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            String categoryName = request.getParameter("categoryName");
            int result = categoryService.createCategory(categoryName); // Delegate to service layer
            if (result == 1) {
                response.sendRedirect("CategoryServlet");
            } else {
                PrintWriter out = response.getWriter();
                out.println("Failed to add category");
            }
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String categoryName = request.getParameter("categoryName");
            categoryService.updateCategory(id, categoryName); // Delegate to service layer
            response.sendRedirect("CategoryServlet");
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            categoryService.deleteCategory(id); // Delegate to service layer
            response.sendRedirect("CategoryServlet");
        } else {
            doGet(request, response); // Delegate other POST requests to GET request
        }
    }
}
