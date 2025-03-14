package cccp.controller;

import java.util.Date;
import java.util.Scanner;

import cccp.service.OnlineOrderServiceInterface;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;
import cccp.view.OnlineOrderView;
import cccp.model.dao.BatchDAO;


public class OnlineOrderController {
	private final OnlineOrderServiceInterface onlineOrderService;
	private final OnlineOrderView onlineOrderView;

    
    public OnlineOrderController(OnlineOrderServiceInterface onlineOrderService, OnlineOrderView onlineOrderView ) {
        this.onlineOrderService = onlineOrderService;
        this.onlineOrderView = onlineOrderView;
    }
    
    public void manageOnlineOrders() {
    	
    	String productId = onlineOrderView.getProductId();
        int quantity = onlineOrderView.getQuantity();
        
        
        BatchSelectionStrategy strategy = new ExpiryBasedSelectionStrategy(new BatchDAO());
        try {
            onlineOrderService.allocateStockForOnlineOrder(productId, quantity, new Date(), strategy);
            onlineOrderView.displayStockAllocated();
        } catch (IllegalStateException e) {
            onlineOrderView.displayError(e.getMessage());
        }
    }

}
