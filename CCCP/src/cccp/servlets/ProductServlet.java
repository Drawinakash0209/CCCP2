package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import cccp.model.Product;
import cccp.model.dao.ProductDAO;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ProductDAO productDAO = new ProductDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Product> products = productDAO.viewAllItemsGUI();
		request.setAttribute("products", products);
		request.getRequestDispatcher("product.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String action = request.getParameter("action");
		
		if("create".equals(action)) {
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
			productDAO.addItem(product);
			response.sendRedirect("ProductServlet");
			}else if("update".equals(action)) {
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
				productDAO.updateItem(product);
				response.sendRedirect("ProductServlet");
				}else if("delete".equals(action)) {
					String id = request.getParameter("id");
					productDAO.removeItem(id);
					response.sendRedirect("ProductServlet");
				}else {
					doGet(request, response);
				}

		}
	}


