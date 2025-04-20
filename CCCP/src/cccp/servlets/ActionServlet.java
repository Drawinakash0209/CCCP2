package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import cccp.factory.ControllerFactory;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.BillDAO;
import cccp.model.dao.OnlineInventoryDAO;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.SaleDAO;
import cccp.model.dao.ShelfDAO;

/**
 * Servlet implementation class ActionServlet
 */
@WebServlet("/ActionServlet")
public class ActionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ControllerFactory controllerFactory;

    @Override
    public void init() throws ServletException {
        // Initialize ControllerFactory with DAO instances
        controllerFactory = new ControllerFactory(
                new BatchDAO(),
                new ShelfDAO(),
                new ProductDAO(),
                new SaleDAO(),
                new BillDAO(),
                new OnlineInventoryDAO()
        );
    }

    /**
     * Constructor
     */
    public ActionServlet() {
        super();
    }

    /**
     * Handles GET requests by redirecting to a default page (e.g., dashboard or error)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to a default page (e.g., dashboard or error page)
        response.sendRedirect("dashboard.jsp"); // Update with your desired page
    }

    /**
     * Handles POST requests by processing the command option through ControllerFactory
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String optionParam = request.getParameter("option");

        try {
            int option = Integer.parseInt(optionParam);

            // Validate option (1 to 11 based on your command options)
            if (option < 1 || option > 11) {
                response.sendRedirect("error.jsp");
                return;
            }

            // Process the command using ControllerFactory
            controllerFactory.processCommand(option, request, response);

        } catch (NumberFormatException e) {
            // Handle invalid option format
            response.sendRedirect("error.jsp");
        } catch (Exception e) {
            // Handle any other exceptions (e.g., timeout, command failure)
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}