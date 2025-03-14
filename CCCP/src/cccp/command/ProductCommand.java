package cccp.command;

import cccp.controller.ProductController;

public class ProductCommand implements Command {
	
	private ProductController productController;
	
	// Constructor to initialize the ProductController
	public ProductCommand(ProductController productController) {
		this.productController = productController; 
	}

	// Executes the product-related action by calling the controller's run method
	@Override
	public void execute() {
		productController.run();
	}

}
