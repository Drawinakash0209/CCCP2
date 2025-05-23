package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import cccp.model.Product;
import cccp.model.User;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.ProductDAO;
import cccp.service.ProductService;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ProductService productService;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServlet() {
        super();
        ProductDAO productDAO = new ProductDAO();
        BatchDAO batchDAO = new BatchDAO();
        this.productService = new ProductService(productDAO, batchDAO);
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
		// TODO Auto-generated method stub
		List<Product> products = productService.getAllProducts();
		request.setAttribute("products", products);
		request.getRequestDispatcher("product.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	    HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
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
			productService.addProduct(product);
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
				productService.updateProduct(product);
				response.sendRedirect("ProductServlet");
				}else if("delete".equals(action)) {
					String id = request.getParameter("id");
					productService.deleteProduct(id);
					response.sendRedirect("ProductServlet");
				}else {
					doGet(request, response);
				}

		}
	}


