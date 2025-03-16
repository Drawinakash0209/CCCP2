package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    	if (action.equals("create")) {
			String name = request.getParameter("name");
			Category category = new Category(0, name);
			categoryDAO.addItem(category);
		}else if (action.equals("update")) {
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			Category category = new Category(id, name);
			categoryDAO.updateItem(category);		
		}else if (action.equals("delete")) {
			int id = Integer.parseInt(request.getParameter("id"));
			categoryDAO.removeItem(id);
		}
    	
    	response.sendRedirect("CategoryServlet");
        doGet(request, response); // Delegate POST request to GET request
    }
}
