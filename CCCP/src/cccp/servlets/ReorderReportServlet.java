package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import cccp.ReportService;
import cccp.ReorderReport;
import cccp.model.Product;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;

@WebServlet("/ReorderReportServlet")
public class ReorderReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportService reportService;

    @Override
    public void init() throws ServletException {
        // Initialize DAO and ReportService
    	ProductDAOInterface productDAO = new ProductDAO(); // Use ProductDAO instead of ProductDAOImpl
        reportService = new ReportService(productDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Generate reorder report
        ReorderReport report = reportService.generateReorderReport();
        List<Product> products = report.getProducts();

        // Set the products as a request attribute
        request.setAttribute("products", products);

        // Forward to JSP
        request.getRequestDispatcher("/reorderReport.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
