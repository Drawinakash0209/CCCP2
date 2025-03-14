package cccp.controller;

import java.util.List;
import java.util.Scanner;

import cccp.service.ProductServiceInterface;
import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;
import cccp.view.BatchView;

public class BatchController {
	private final BatchView view;
	private final BatchDAOInterface dao;
	private final ProductServiceInterface productService;
	
	
	public BatchController(BatchView view, BatchDAOInterface dao, ProductServiceInterface productService) {
		this.view = view;
		this.dao = dao;
		this.productService = productService;
	}
	
	public void run() {
		int choice  = view.showMenuAndUserChoice();
		
		
		switch(choice) {
		case 1 ->{
			String productId = view.getProductId();
			
			  // Validate if product exists
            if ( productId == null) {
                System.out.println("Invalid product ID.");
                return;
            }
            
			Batch batch = view.getBatchDetails();
			
			  // Validate batch details
            if (batch.getQuantity() <= 0) {
                System.out.println("Batch quantity must be greater than 0.");
                return;
            }
            
            if (batch.getExpiryDate().before(batch.getPurchaseDate())) {
                System.out.println("Expiry date must be after the purchase date.");
                return;
            }
            
            
			productService.addBatchToProduct(productId, batch);
			break;
		}
		case 2 ->{
			String productId = view.getProductId();
			List<Batch> batches = dao.getBatchesByProductId(productId);
			view.viewAllBatches(batches);
			break;
		}
		
		case 3->{
			System.out.println("Exit");
			return;
		}
		default -> System.out.println("Invalid entry");

		}
	
	}

}
