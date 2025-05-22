package cccp.factory;

import cccp.command.*;
import cccp.model.dao.*;
import cccp.queue.CommandProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class ControllerFactory {
    private final BatchDAOInterface batchDAO;
    private final ShelfDAOInterface shelfDAO;
    private final ProductDAOInterface productDAO;
    private final SaleDAOInterface saleDAO;
    private final BillDAOInterface billDAO;
    private final OnlineInventoryDAOInterface onlineDAO;

    public ControllerFactory(BatchDAOInterface batchDAO, ShelfDAOInterface shelfDAO, ProductDAOInterface productDAO, SaleDAOInterface saleDAO, BillDAOInterface billDAO, OnlineInventoryDAOInterface onlineDAO) {
        this.batchDAO = batchDAO;
        this.shelfDAO = shelfDAO;
        this.productDAO = productDAO;
        this.saleDAO = saleDAO;
        this.billDAO = billDAO;
        this.onlineDAO = onlineDAO;
    }

    public void processCommand(int option, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Command command = getCommand(option, request, response);
        if (command == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid command option");
            return;
        }

        CommandProcessor.addCommand(command, request, response);

        try {
            CommandProcessor.CommandResult result = command.getResultFuture().get(30, TimeUnit.SECONDS);
            if ("failed".equals(result.status)) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, result.message);
            }
            // Success case: Command's execute() already forwards to the appropriate servlet/JSP
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Command processing failed: " + e.getMessage());
        }
    }

    private Command getCommand(int option, HttpServletRequest request, HttpServletResponse response) {
        switch (option) {
            case 1:
                return new CategoryCommand(request, response);
            case 2:
                return new ProductCommand(request, response);
            case 3:
                return new BatchCommand(request, response);
            case 4:
                return new ShelfCommand(request, response);
            case 5:
                return new OnlineOrderCommand(request, response);
            case 6:
                return new BillCommand(request, response);
            case 7:
                return new BillReportCommand(request, response);
            case 8:
                return new SalesReportCommand(request, response);
            case 9:
                return new ReorderReportCommand(request, response);
            case 10:
                return new StockReportCommand(request, response);
            case 11:
                return new ReshelveReportCommand(request, response);
            default:
                return null;
        }
    }
}