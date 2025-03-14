package cccp.controller;

import cccp.model.Bill;
import cccp.model.dao.BillDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.BillingServiceInterface;
import cccp.view.BillView;

public class BillController {
	private final BillView view;
	private final BillingServiceInterface billingService;
	private final ProductDAOInterface productDAO;
    
	
	  public BillController(BillView view, BillingServiceInterface billingService, ProductDAOInterface productDAO) {
	        this.view = view;
	        this.billingService = billingService;
	        this.productDAO = productDAO;  // Initialize ProductDAO
	    }
	  
	  
	public void run() {
        int choice = view.showMenuAndUserChoice();
        if (choice == 1) {
        	Bill bill = view.getBillDetails(billingService, productDAO);
        	view.displayBillDetails(bill);  // Display the bill details
        }
    }

}
