package cccp;

import java.util.Scanner;

import cccp.command.Command;
import cccp.factory.CustomerControllerFactory;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.BillDAO;
import cccp.model.dao.OnlineInventoryDAO;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.SaleDAO;
import cccp.model.dao.ShelfDAO;

public class CustomerUserInterface {
	private Scanner scanner;
    private CustomerControllerFactory customerControllerFactory;
    
    
    public CustomerUserInterface(Scanner scanner) {
    	this.scanner = scanner;
    	this.customerControllerFactory = new CustomerControllerFactory(
    			new BatchDAO(),
    			new BillDAO(),
    			new ProductDAO(),
    			new SaleDAO(),
    			new OnlineInventoryDAO()
    			);
    }
    
    public void run() {
    	System.out.println("Welcome to online Portal");
    	
    	try {
    		System.out.println("Select an option");
    		System.out.println("1. online shopping");
    		
    		int option = scanner.nextInt();
            scanner.nextLine(); 
            
            if (option < 1 || option > 1) {
                System.out.println("Invalid option. Please select a number between 1 and 7.");
                return;
            }
            
            Command command = customerControllerFactory.getCommand(option);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Command execution failed. Please try again.");
            }
            
    	}catch (Exception e) {
            System.out.println("An unexpected error occurred. Please try again");
        }
    }


}
