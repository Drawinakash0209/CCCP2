package cccp;

import java.util.InputMismatchException;
import java.util.Scanner;

import cccp.command.Command;
import cccp.factory.ControllerFactory;
import cccp.model.dao.*;

public class UserInterface {
    private Scanner scanner;
    private ControllerFactory controllerFactory;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
        this.controllerFactory = new ControllerFactory(
            scanner,
            new BatchDAO(),
            new ShelfDAO(),
            new ProductDAO(),
            new SaleDAO(),
            new BillDAO(),
            new OnlineInventoryDAO()
        );
    }

    public void run() {
        System.out.println("Welcome to SYOS POS System");

        while (true) {
        try {
            System.out.println("Select an Option:");
            System.out.println("1. Category");
            System.out.println("2. Item");
            System.out.println("3. Batch");
            System.out.println("4. Shelf Inventory Management");
            System.out.println("5. Online Inventory Management");
            System.out.println("6. create Bill");
            System.out.println("7. Generate Bill Report");
            System.out.println("8. Generate Sales Report");
            System.out.println("9. Generate Reorder Report");
            System.out.println("10. Generate Stock Report");
            System.out.println("11. Generate Reshelve Report Report");

            int option = scanner.nextInt();
            scanner.nextLine(); 

            if (option < 1 || option > 12) {
                System.out.println("Invalid option. Please select a number between 1 and 10.");
                return;
            }

            Command command = controllerFactory.getCommand(option);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Command execution failed. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter a number.");
            scanner.nextLine();
        } catch (NullPointerException e) {
            System.out.println("Error: Please enter a number");
        } catch (IllegalStateException e) {
            System.out.println("Error: Please try again later.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred. Please try again");
        }
    }
    }
}