package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import cccp.model.Batch;
import cccp.model.User;
import cccp.model.dao.BatchDAO;
import cccp.service.BatchService;

@WebServlet("/BatchServlet")
public class BatchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BatchService batchService;

    public BatchServlet() {
        super();
        BatchDAO batchDAO = new BatchDAO();
        this.batchService = new BatchService(batchDAO);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
        request.getRequestDispatcher("batch.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp?error=Please login first");
            return;
        }
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            try {
                String productId = request.getParameter("product_id");
                String batchId = request.getParameter("batch_id");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                String purchaseDateStr = request.getParameter("purchase_date");
                String expiryDateStr = request.getParameter("expiry_date");

                batchService.addBatch(productId, batchId, quantity, purchaseDateStr, expiryDateStr);
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
            List<Batch> batchList = batchService.getBatchesByProductId(productId);
            request.setAttribute("batchList", batchList);
            request.getRequestDispatcher("batch.jsp").forward(request, response);
        } else {
            doGet(request, response);
        }
    }
}