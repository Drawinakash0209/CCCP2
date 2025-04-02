// Updated SalesReportServlet.java
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

import cccp.ReportService;
import cccp.SaleReport;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.SaleDAO;

@WebServlet("/SalesReportServlet")
public class SalesReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportService reportService;

    @Override
    public void init() throws ServletException {
        super.init();
        reportService = new ReportService(new SaleDAO(), new ProductDAO());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inputDate = request.getParameter("date");
        if (inputDate != null && !inputDate.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = formatter.parse(inputDate);
                SaleReport saleReport = reportService.generateSalesReport(date);
                request.setAttribute("saleReport", saleReport);
                request.setAttribute("inputDate", inputDate);
            } catch (ParseException e) {
                request.setAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        request.getRequestDispatcher("salesReport.jsp").forward(request, response);
    }
}
