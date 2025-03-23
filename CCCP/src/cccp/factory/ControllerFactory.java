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
import cccp.service.*;
import cccp.view.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerFactory {
    private final Scanner scanner;
    private final BatchDAOInterface batchDAO;
    private final ShelfDAOInterface shelfDAO;
    private final ProductDAOInterface productDAO;
    private final SaleDAOInterface saleDAO;
    private final BillDAOInterface billDAO;
    private final OnlineInventoryDAOInterface onlineDAO;

    public ControllerFactory(Scanner scanner, BatchDAOInterface batchDAO, ShelfDAOInterface shelfDAO, ProductDAOInterface productDAO, SaleDAOInterface saleDAO, BillDAOInterface billDAO, OnlineInventoryDAOInterface onlineDAO) {
        this.scanner = scanner;
        this.batchDAO = batchDAO;
        this.shelfDAO = shelfDAO;
        this.productDAO = productDAO;
        this.saleDAO = saleDAO;
        this.billDAO = billDAO;
        this.onlineDAO = onlineDAO;
    }

    public Command getCommand(int option, HttpServletRequest request, HttpServletResponse response) {
    	
    	
        switch (option) {
            case 1:
                return new CategoryCommand(request, response);
            case 2:
                return new ProductCommand(request, response);
                
            case 3:
            	return new BatchCommand(request, response);

            case 4:
                ShelfService shelfService = new ShelfService(batchDAO, shelfDAO);
                ProductService productService = new ProductService(productDAO, batchDAO);
                shelfService.addRestockListener(productService);
                ShelfView shelfView = new ShelfView(scanner);
                return new ShelfCommand(new ShelfController(shelfService, shelfView));
            case 5:
                OnlineOrderService onlineService = new OnlineOrderService(batchDAO, onlineDAO);
                ProductService productService1 = new ProductService(productDAO, batchDAO);
                onlineService.addRestockListener(productService1);
                OnlineOrderView onlineOrderView = new OnlineOrderView(scanner);
                return new OnlineOrderCommand(new OnlineOrderController(onlineService, onlineOrderView));
            case 6:
                ShelfService shelfService2 = new ShelfService(batchDAO, shelfDAO);
                BillingService billingService2 = new BillingService(billDAO, productDAO, shelfService2, saleDAO);
                return new BillCommand(new BillController(new BillView(billingService2), billingService2, productDAO));
            case 7:
                return new BillReportCommand();
            case 8:
                return new SalesReportCommand();
            case 9:
            	return new ReorderReportCommand(new ReportService(new ProductDAO()));
            case 10:
            	return new StockReportCommand(new ReportService(new ProductDAO()) );
            	
            case 11:
            	return new ReshelveReportCommand(new ReportService(productDAO, shelfDAO));
            default:
                return null;
        }
    }
}