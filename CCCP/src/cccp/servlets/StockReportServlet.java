package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import cccp.ReportService;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.StockItem;

/**
 * Servlet implementation class StockReportServlet
 */
@WebServlet("/StockReportServlet")
public class StockReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReportService reportService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
//    public StockReportServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

	@Override
	public void init() throws ServletException {
		ProductDAOInterface productDAO = new ProductDAO();
		reportService = new ReportService(productDAO);

	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<StockItem> stockItems = reportService.generateStockReport();
		for (StockItem stockItem : stockItems) {
			System.out.println(stockItem.getProductName());
		}
		request.setAttribute("stockItems", stockItems);
		request.getRequestDispatcher("StockReport.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
