package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import cccp.BillReport;
import cccp.ReportService;
import cccp.model.Bill;
import cccp.model.User;
import cccp.model.dao.BillDAO;

/**
 * Servlet implementation class BillReportServlet
 */
@WebServlet("/BillReportServlet")
public class BillReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReportService reportService;
	
    public BillReportServlet() {
        super();
        reportService = new ReportService(new BillDAO());
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
		
		// Generate bill report data
		BillReport billReport = reportService.generateAllBillReports();
		List<Bill> bills = billReport.getBills();
		
		// Store data in request scope
		request.setAttribute("bills", bills);
		
		// Forward request to JSP
		request.getRequestDispatcher("billReport.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
		doGet(request, response);
	}
}
