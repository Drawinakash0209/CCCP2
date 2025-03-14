package cccp.factory;


import cccp.OnlinePurchaseController;
import cccp.command.Command;
import cccp.command.OnlinePurchaseCommand;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.OnlineInventoryDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;
import cccp.service.BillingService;
import cccp.service.OnlineOrderService;
import cccp.view.BillView;

public class CustomerControllerFactory {
	private final BatchDAOInterface batchDAO;
	private final BillDAOInterface billDAO; 
	private final ProductDAOInterface productDAO;
	private final SaleDAOInterface saleDAO;
	private final OnlineInventoryDAOInterface onlineDAO;
	
	
	  public CustomerControllerFactory(BatchDAOInterface batchDAO, BillDAOInterface billDAO, ProductDAOInterface productDAO, SaleDAOInterface saleDAO, OnlineInventoryDAOInterface onlineDAO) {
		  this.batchDAO = batchDAO;
		  this.billDAO = billDAO;
		  this.productDAO = productDAO;
		  this.saleDAO = saleDAO;
		  this.onlineDAO = onlineDAO;
		  
	    }
	  
	  public Command getCommand(int option) {
		  switch(option) {
		  case 1:
			  OnlineOrderService onlineOrderService = new OnlineOrderService(batchDAO, onlineDAO);
			  BillingService billingService = new BillingService(billDAO, productDAO, onlineOrderService, saleDAO);
			  return new OnlinePurchaseCommand(new OnlinePurchaseController(new BillView(billingService), billingService, productDAO));
          default:
        	  return null;
		  }
	  }
	

}
