package cccp.view;

import java.util.Scanner;

public class ShelfView {
	
    private final Scanner scanner;

    public ShelfView(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public String getProductId() {
        System.out.print("Enter Product ID to restock: ");
        return scanner.nextLine().trim();
    }
    
    public int getQuantity() {
        System.out.print("Enter quantity: ");
        int quantity = 0;
        try {
            quantity = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character
        } catch (Exception e) {
            System.out.println("Error: Invalid quantity. Please enter a valid number.");
            scanner.nextLine(); // Clear the invalid input
        }
        return quantity;
    }
    
    public void displayError(String message) {
        System.out.println("Error: " + message);
    }

    public void displayRestockedMessage() {
        System.out.println("Shelf restocked successfully.");
    }

    
    

}
