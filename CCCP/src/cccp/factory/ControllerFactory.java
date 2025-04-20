package cccp.factory;

import java.util.Scanner;

import cccp.ReportService;
import cccp.command.BatchCommand;
import cccp.command.BillCommand;
import cccp.command.BillReportCommand;
import cccp.command.CategoryCommand;
import cccp.command.Command;
import cccp.command.OnlineOrderCommand;
import cccp.command.ProductCommand;
import cccp.command.ReorderReportCommand;
import cccp.command.ReshelveReportCommand;
import cccp.command.SalesReportCommand;
import cccp.command.ShelfCommand;
import cccp.command.StockReportCommand;
import cccp.controller.*;
import cccp.model.dao.*;
import cccp.queue.CommandProcessor;
import cccp.service.*;
import cccp.view.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        String resultKey = command.getResultKey();
        int maxAttempts = 10;
        int attempt = 0;
        CommandProcessor.CommandResult result = null;

        while (attempt < maxAttempts) {
            result = CommandProcessor.getCommandResult(resultKey);
            if (result != null && !"pending".equals(result.status)) {
                break;
            }
            Thread.sleep(500);
            attempt++;
        }

        CommandProcessor.clearCommandResult(resultKey);

        if (result == null || "pending".equals(result.status)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Command processing timed out");
        } else if ("failed".equals(result.status)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, result.message);
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