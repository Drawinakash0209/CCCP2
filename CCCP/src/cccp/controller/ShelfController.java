package cccp.controller;

import java.util.Date;
import java.util.Scanner;

import cccp.model.dao.BatchDAO;
import cccp.service.ShelfServiceInterface;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;
import cccp.view.ShelfView;

public class ShelfController {
	private final ShelfServiceInterface shelfService;
	  private final ShelfView shelfView;
    
    
    public ShelfController(ShelfServiceInterface shelfService, ShelfView shelfView) {
    	  this.shelfService = shelfService;
          this.shelfView = shelfView;
    }
    
    
    public void manageShelf() {
    	String productId = shelfView.getProductId();
    	
    	if (productId.isEmpty()) {
            shelfView.displayError("Product ID cannot be empty.");
            return;
        }
    	
    	int quantity = shelfView.getQuantity();
    	
    	 if (quantity <= 0) {
             shelfView.displayError("Quantity must be greater than zero.");
             return;
         }
    	 
    	 
    	 BatchSelectionStrategy strategy = new ExpiryBasedSelectionStrategy(new BatchDAO());
    	
    	 try {
             shelfService.restockShelf(productId, quantity, new Date(), strategy);
             shelfView.displayRestockedMessage();
         } catch (IllegalStateException e) {
             shelfView.displayError(e.getMessage());
         }
    }

}
