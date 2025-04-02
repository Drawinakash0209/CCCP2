package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cccp.model.Batch;
import cccp.model.dao.BatchDAO;

/**
 * Servlet implementation class BatchServlet
 */
@WebServlet("/BatchServlet")
public class BatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final BatchDAO batchDAO = new BatchDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BatchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("batch.jsp").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String action = request.getParameter("action");

	    if ("create".equals(action)) {
	        try {
	            String productId = request.getParameter("product_id");
	            String batchId = request.getParameter("batch_id");
	            int quantity = Integer.parseInt(request.getParameter("quantity"));
	            String purchaseDateStr = request.getParameter("purchase_date");
	            String expiryDateStr = request.getParameter("expiry_date");

	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            Date purchaseDate = dateFormat.parse(purchaseDateStr);
	            Date expiryDate = dateFormat.parse(expiryDateStr);

	            Batch batch = new Batch(batchId, quantity, purchaseDate, expiryDate);
	            batchDAO.addBatch(batch, productId);

	            // Redirect to refresh the page with updated batch list
	            response.sendRedirect("BatchServlet");
	        } catch (ParseException e) {
	            e.printStackTrace();
	            request.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD.");
	            request.getRequestDispatcher("batch.jsp").forward(request, response);
	        } catch (NumberFormatException e) {
	            e.printStackTrace();
	            request.setAttribute("errorMessage", "Invalid quantity format.");
	            request.getRequestDispatcher("batch.jsp").forward(request, response);
	        }
	    } else if ("searchBatch".equals(action)) {
	        String productId = request.getParameter("product_id");
	        List<Batch> batchList = batchDAO.getBatchesByProductId(productId);
	        request.setAttribute("batchList", batchList);
	        request.getRequestDispatcher("batch.jsp").forward(request, response);
	    } else {
	        doGet(request, response);
	    }
	}
}
