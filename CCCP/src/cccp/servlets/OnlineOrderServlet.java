package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import cccp.model.dao.BatchDAO;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.OnlineInventoryDAO;
import cccp.model.dao.OnlineInventoryDAOInterface;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.OnlineOrderService;
import cccp.service.OnlineOrderServiceInterface;
import cccp.service.ProductService;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;

/**
 * Servlet implementation class OnlineOrderServlet
 */
@WebServlet("/OnlineOrderServlet")
public class OnlineOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OnlineOrderServiceInterface orderService;
	private ProductService productService;
	
	
	@Override
	public void init() throws ServletException {
		BatchDAOInterface batchDAO = new BatchDAO();
		OnlineInventoryDAOInterface onlineDAO = new OnlineInventoryDAO();
		ProductDAOInterface productDAO = new ProductDAO();
		orderService = new OnlineOrderService(batchDAO, onlineDAO);
		productService = new ProductService(productDAO, batchDAO);
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OnlineOrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("onlineStockRestock.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		  String productId = request.getParameter("productId");
		  String quantityStr = request.getParameter("quantity");
		  

		    if (productId == null || productId.trim().isEmpty()) {
		        request.setAttribute("error", "Product ID is required");
		        request.getRequestDispatcher("onlineStockRestock.jsp").forward(request, response);
		        return;
		    }
		    
		    int quantity;
		    try {
		        quantity = Integer.parseInt(quantityStr);
		        if (quantity <= 0) {
		            request.setAttribute("error", "Quantity must be greater than zero");
		            request.getRequestDispatcher("onlineStockRestock.jsp").forward(request, response);
		            return;
		        }
		    } catch (NumberFormatException e) {
		        request.setAttribute("error", "Invalid quantity");
		        request.getRequestDispatcher("onlineStockRestock.jsp").forward(request, response);
		        return;
		    }
		    
		    BatchSelectionStrategy strategy = new ExpiryBasedSelectionStrategy(new BatchDAO());
		    try {
		        orderService.allocateStockForOnlineOrder(productId, quantity, new java.util.Date(), strategy);
		        request.setAttribute("success", "Product restocked successfully");
		    } catch (Exception e) {
		        request.setAttribute("error", "Error restocking product: " + e.getMessage());
		    }
		    
		    orderService.addRestockListener(productService);
		    request.getRequestDispatcher("onlineStockRestock.jsp").forward(request, response);

	}

}
