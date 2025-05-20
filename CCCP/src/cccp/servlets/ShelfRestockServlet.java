package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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

/**
 * Servlet implementation class ShelfServlet
 */
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
	   
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShelfRestockServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("shelfRestock.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String productId = request.getParameter("productId");
	    String quantityStr = request.getParameter("quantity");
	    System.out.println("doPost: productId=" + productId + ", quantity=" + quantityStr + ", thread=" + Thread.currentThread().getName());

	    if (productId == null || productId.trim().isEmpty()) {
	        System.out.println("Validation error: Product ID is required");
	        request.setAttribute("error", "Product ID is required");
	        request.getRequestDispatcher("shelfRestock.jsp").forward(request, response);
	        return;
	    }

	    int quantity;
	    try {
	        quantity = Integer.parseInt(quantityStr);
	        if (quantity <= 0) {
	            System.out.println("Validation error: Quantity must be greater than zero");
	            request.setAttribute("error", "Quantity must be greater than zero");
	            request.getRequestDispatcher("shelfRestock.jsp").forward(request, response);
	            return;
	        }
	    } catch (NumberFormatException e) {
	        System.out.println("Validation error: Invalid quantity");
	        request.setAttribute("error", "Invalid quantity");
	        request.getRequestDispatcher("shelfRestock.jsp").forward(request, response);
	        return;
	    }

	    BatchSelectionStrategy strategy = new ExpiryBasedSelectionStrategy(new BatchDAO());
	    try {
	        System.out.println("Attempting to restock: productId=" + productId + ", quantity=" + quantity);
	        shelfService.restockShelf(productId, quantity, new java.util.Date(), strategy);
	        System.out.println("Restock successful: productId=" + productId);
	        request.setAttribute("message", "Shelf restocked successfully");
	    } catch (IllegalStateException e) {
	        System.out.println("Restock failed: " + e.getMessage());
	        request.setAttribute("error", e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Unexpected error during restock: " + e.getMessage());
	        request.setAttribute("error", "Unexpected error: " + e.getMessage());
	    }

	    shelfService.addRestockListener(productService);
	    System.out.println("Forwarding to shelfRestock.jsp");
	    request.getRequestDispatcher("shelfRestock.jsp").forward(request, response);
	}

}
