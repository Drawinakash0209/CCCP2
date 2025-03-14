package cccp;

import cccp.model.Bill;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.BillingServiceInterface;
import cccp.view.BillView;

public class OnlinePurchaseController {
	private final BillView view;
	private final BillingServiceInterface billingService;
	private final ProductDAOInterface productDAO;

    public OnlinePurchaseController(BillView view, BillingServiceInterface billingService, ProductDAOInterface productDAO) {
        this.view = view;
        this.billingService = billingService;
        this.productDAO = productDAO;  // Initialize ProductDAO
    }

    public void run() {
        int choice = view.showOnlineMenuUserChoice();
        if (choice == 1) {
        	Bill bill = view.getOnlineBillDetails(billingService, productDAO);
        	view.displayOnlineBillDetails(bill);
        }
    }
}