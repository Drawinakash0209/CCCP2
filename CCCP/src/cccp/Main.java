package cccp;
//

import java.util.Scanner;

import cccp.factory.AuthenticationFactory;
import cccp.model.User;
import cccp.model.dao.UserDAO;

import cccp.strategy.AuthenticationStrategy;


public class Main {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();

        System.out.println("1. Register as customer");
        System.out.println("2. Login");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            System.out.println("Enter username:");
            String username = scanner.nextLine();

            System.out.println("Enter password:");
            String password = scanner.nextLine();

            UserRegistrationFactory.getCustomerRegistrationStrategy().register(username, password);
            System.out.println("Registration successful");
        }

        // Login loop
        while (true) {
            System.out.println("\nEnter username:");
            String username = scanner.nextLine();

            System.out.println("Enter password:");
            String password = scanner.nextLine();

            User user = userDAO.getUserByUsername(username);

            if (user != null) {
                AuthenticationStrategy strategy = AuthenticationFactory.getAuthenticationStrategy(user.getUserType());

                if (strategy.authentication(user, password)) {
                    System.out.println("Login successful! Welcome, " + user.getUserType());

                    if (user.getUserType().equalsIgnoreCase("Employee")) {
                        UserInterface ui = new UserInterface(scanner);
                        ui.run();
                    } else {
                        CustomerUserInterface customerUi = new CustomerUserInterface(scanner);
                        customerUi.run();
                    }
                    break;
                } else {
                    System.out.println("Invalid password. Try again.");
                }
            } else {
                System.out.println("User not found. Try again.");
            }
        }
        
        scanner.close();
    }
    	
    }
