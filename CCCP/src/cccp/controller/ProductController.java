package cccp.controller;

import cccp.model.Product;
import cccp.model.dao.ProductDAOInterface;
import cccp.view.ProductView;

public class ProductController {
	private final ProductView view;
	private final ProductDAOInterface dao;
	
	public ProductController(ProductView view, ProductDAOInterface dao) {
		this.view = view;
		this.dao = dao;		
	}
	
	public void run() {
		while(true) {
			int choice = view.showMenuAndUserChoice();
			
			switch(choice) {
			case 1->{
				Product product = view.getProductDetails();
				dao.addItem(product);	
			}
			case 2->{
				dao.viewAllItems();
				
			}
			case 3->{
				String id = view.getProductId();
				Product product = dao.searchProductById(id);
				
			}
			case 4->{
				Product product = view.getUpdatedProductDetails();
				dao.updateItem(product);
			}
			case 5->{
				String productId = view.getProductId();
				dao.removeItem(productId);
			}
			case 6->{
				System.out.println("Exit");
				return;
			}
			default -> System.out.println("Invalid entry");
			}
		}
	}
	

	
}
