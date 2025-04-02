package cccp;

import java.util.List;

import cccp.model.Product;

public class ReorderReport {
	private List<Product> products;
	
	public ReorderReport(List<Product> products) {
		this.products = products;
	}
	
	public List<Product> getProducts() {
		return products;
	}	
	
	public void displayReport() {
		System.out.println("Reorder Report: Products with stock below reorder level (50 items):");
		System.out.printf("%-10s %-20s%n", "Product ID", "Name");
	    System.out.println("----------------------------------------------");
		for (Product product : products) {
			System.out.println("Product ID :	" + product.getId() + "		Name :	" + product.getName());
		}
	}

}
