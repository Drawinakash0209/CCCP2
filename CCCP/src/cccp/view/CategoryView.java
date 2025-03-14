package cccp.view;

import java.util.Scanner;

import cccp.model.Category;

public class CategoryView {
	private final Scanner scanner = new Scanner(System.in);
	
	public int showMenuAndUserChoice() {
		System.out.println("\n === Category Management ===");
		System.out.println("1. Add Category");
		System.out.println("2. View all Categories");
		System.out.println("3. Update Category");
		System.out.println("4. Delete Category");
		System.out.println("5. Exit Category");
		System.out.println("Enter choice number");
		
		return scanner.nextInt();
	}
	
	public Category getCategoryDetails() {
		System.out.println("Enter Category Name");
		String name = scanner.next();
		return new Category(0, name);
		
	}
	
	public int getCategoryId() {
		System.out.println("Enter Category ID");
		return scanner.nextInt();
	}
	
	public String getUpdatedName() {
		System.out.println("Enter new Category Name");
		return scanner.next();
	}

}
