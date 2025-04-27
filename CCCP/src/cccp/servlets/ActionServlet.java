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

@WebServlet("/ActionServlet")
public class ActionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ControllerFactory controllerFactory;

    @Override
    public void init() throws ServletException {
        controllerFactory = new ControllerFactory(
                new BatchDAO(),
                new ShelfDAO(),
                new ProductDAO(),
                new SaleDAO(),
                new BillDAO(),
                new OnlineInventoryDAO()
        );
    }

    public ActionServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String optionParam = request.getParameter("option");

        if (optionParam != null) {
            try {
                int option = Integer.parseInt(optionParam);
                if (option < 1 || option > 11) {
                    response.sendRedirect("error.jsp");
                    return;
                }
                controllerFactory.processCommand(option, request, response);
                return; // Exit after processing the command
            } catch (NumberFormatException e) {
                response.sendRedirect("error.jsp");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
                return;
            }
        }

        // Default redirect if no option is provided
        response.sendRedirect("dashboard.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String optionParam = request.getParameter("option");

        try {
            int option = Integer.parseInt(optionParam);
            if (option < 1 || option > 11) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid command option");
                return;
            }
            controllerFactory.processCommand(option, request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid option format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Command processing failed: " + e.getMessage());
        }
    }
}