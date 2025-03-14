package cccp.view;

import java.util.Scanner;

public class OnlineOrderView{
	
	private final Scanner scanner;

    public OnlineOrderView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getProductId() {
        System.out.print("Enter Product ID for online order: ");
        return scanner.nextLine();
    }

    public int getQuantity() {
        System.out.print("Enter quantity: ");
        return scanner.nextInt();
    }

    public void displayStockAllocated() {
        System.out.println("Stock allocated for online order.");
    }

    public void displayError(String message) {
        System.out.println("Error: " + message);
    }

}
