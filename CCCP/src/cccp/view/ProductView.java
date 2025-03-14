package cccp.view;

import java.util.Scanner;

import cccp.model.Product;

public class ProductView {

	private final Scanner scanner = new Scanner(System.in);
	
	public int showMenuAndUserChoice() {
		 System.out.println("\n === Product Management ===");
         System.out.println("1. Create Product");
         System.out.println("2. View All Products");
         System.out.println("3. Search Product by ID");
         System.out.println("4. Update Product");
         System.out.println("5. Delete Product");
         System.out.println("6. Exit Product");
         System.out.println("Enter Choice Number");
         
         return scanner.nextInt();
	}
	
	public Product getProductDetails(){
		System.out.println("Enter Product unique ID");
		String productid = scanner.next();
		scanner.nextLine();
	    System.out.println("Enter Product name");
	    String productName = scanner.next();
	    scanner.nextLine();  
	    System.out.println("Enter Product price:");
	    double productPrice = scanner.nextDouble();
	    System.out.println("Enter Category ID:");
	    int categoryId = scanner.nextInt();
	    System.out.println("Enter Reorder Level:");
	    int reorderLevel = scanner.nextInt();  // Get reorder level
	    return new Product.Builder()
	    		.setId(productid)
	            .setName(productName)
	            .setPrice(productPrice)
	            .setCategoryId(categoryId)
	            .setReorderLevel(reorderLevel)  // Set reorder level
	            .build();
	}

	
	public String getProductName() {
		System.out.println("Enter Product Name");
		return scanner.next();
	}
	
	public String getProductId() {
		System.out.println("Enter Product Id");
		return scanner.next();
	}
	
	public Product getUpdatedProductDetails() {
		System.out.println("Enter the Id of the product you want to change");
		String productId = scanner.next();
		scanner.nextLine();
		System.out.println("Enter Product Name");
		String productName = scanner.next();
		System.out.println("Enter Product price:");
        double productPrice = scanner.nextDouble();
        System.out.println("Enter Category ID:");
        int categoryId = scanner.nextInt();
        System.out.println("Enter product Re-order Level");
        int productReorderLevel = scanner.nextInt();
        
        
        return new Product.Builder()
        		.setId(productId)
        		.setName(productName)
        		.setPrice(productPrice)
        		.setCategoryId(categoryId)
        		.setReorderLevel(productReorderLevel)
        		.build();
	}
}
