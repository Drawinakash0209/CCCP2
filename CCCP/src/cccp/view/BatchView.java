package cccp.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import cccp.model.Batch;

public class BatchView {
	
	private final Scanner scanner = new Scanner(System.in);
	
	public int showMenuAndUserChoice() {
		System.out.println("\n === Batch Management === ");
        System.out.println("1. Add Batch");
        System.out.println("2. View Batches for Product");
        System.out.println("3. Exit");
        
        System.out.println("Enter choice number");
        
        return scanner.nextInt();
	}
	
	public Batch getBatchDetails() {
		scanner.nextLine();
		System.out.println("Enter batch code:");
	    String batchCode = scanner.nextLine();  
        System.out.println("Enter quantity:");
        int batchQuantity = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter purchase date (yyyy-MM-dd):");
        String purchaseDateString = scanner.nextLine();
        System.out.println("Enter expiry date (yyyy-MM-dd):");
        String expiryDateString = scanner.nextLine();
        Date purchaseDate = null;
        Date expiryDate = null;
        try {
            purchaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(purchaseDateString);
            expiryDate = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Batch(batchCode, batchQuantity, purchaseDate, expiryDate);
	}
	
	public String getProductId() {
		System.out.println("Enter product ID for the batch:");
		return scanner.next();
		
	}
	
	public String getBatchId() {
		System.out.println("Enter batch code:");
		return scanner.nextLine();
	}

	public void viewAllBatches(List<Batch> batches) {
		System.out.printf("%-10s | %-10s | %-10s | %-10s\n", "Batch Code", "Quantity", "Purchase Date", "Expiry Date");
		  for (Batch batch : batches) {
              System.out.printf("%-10s | %-10d | %-10s | %-10s\n", batch.getBatchcode(), batch.getQuantity(), batch.getPurchaseDate(), batch.getExpiryDate());
          }
	}
	
//	public Batch getUpdatedBatchDetails() {
//		System.out.println("Enter batch code:");
//	    String batchCode = scanner.nextLine();
//		scanner.nextLine();    
//        System.out.println("Enter quantity:");
//        int batchQuantity = scanner.nextInt();
//        scanner.nextLine();
//        System.out.println("Enter purchase date (yyyy-MM-dd):");
//        String purchaseDateString = scanner.nextLine();
//        System.out.println("Enter expiry date (yyyy-MM-dd):");
//        String expiryDateString = scanner.nextLine();
//        Date purchaseDate = null;
//        Date expiryDate = null;
//        try {
//            purchaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(purchaseDateString);
//            expiryDate = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return new Batch(batchCode, batchQuantity, purchaseDate, expiryDate);
//	}
}
