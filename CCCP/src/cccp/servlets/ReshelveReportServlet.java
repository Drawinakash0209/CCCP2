package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import cccp.ReportService;
import cccp.ReshelveReport;
import cccp.model.User;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.ShelfDAO;
import cccp.model.dao.ShelfDAOInterface;

/**
 * Servlet implementation class ReshelveReport
 */
@WebServlet("/ReshelveReportServlet")
public class ReshelveReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReportService reportService;
	
	@Override
	public void init() throws ServletException {
		ProductDAOInterface productDAO = new ProductDAO();
		ShelfDAOInterface shelfDAO = new ShelfDAO();
		reportService = new ReportService(productDAO, shelfDAO);
	
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
		ReshelveReport report = reportService.generateReshelveReport();
		request.setAttribute("report", report);
		request.getRequestDispatcher("/reshelveReport.jsp").forward(request, response);
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
		doGet(request, response);
	}

}
