package cccp.view;


import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import cccp.PercentageDiscount;
import cccp.model.Bill;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.BillingServiceInterface;

public class BillView {

    private final BillingServiceInterface billingService;
    private final Scanner scanner = new Scanner(System.in);

    public BillView(BillingServiceInterface billingService) {
        this.billingService = billingService;
    }

    public int showMenuAndUserChoice() {
        System.out.println("\n === Bill Management === ");
        System.out.println("1. Create Bill");
        System.out.println("Enter choice number");
        return scanner.nextInt();
    }

    
    
    public int showOnlineMenuUserChoice() {
        System.out.println("\n === Online shopping === ");
        System.out.println("1. purchase items");
        System.out.println("Enter choice number");
        return scanner.nextInt();
    }

    
    public Bill getBillDetails(BillingServiceInterface billingService, ProductDAOInterface productDAO){
        List<Bill.BillItem> billItems = new ArrayList<>();

        while(true) {
            System.out.println("Enter Product Code or Enter 0 to stop");
            String productCode = scanner.next();
            if (productCode.equals("0")) {
                break;
            }
            System.out.println("Enter quantity");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            Bill.BillItem billItem = billingService.createBillItem(productCode, quantity);
            billItems.add(billItem);
        }
        
     // Get the discount rate from the user
        System.out.print("Enter discount rate (0-100): ");
        double discountRate = scanner.nextDouble();
        billingService.setDiscount(new PercentageDiscount(discountRate));
        
        System.out.print("Enter cash tendered: ");
        double cashTendered = scanner.nextDouble();

        Bill bill = billingService.createBill(billItems, cashTendered);
        
        return bill;
    }
    
    public Bill getOnlineBillDetails(BillingServiceInterface billingService, ProductDAOInterface productDAO) {
    	List<Bill.BillItem> billItems = new ArrayList<>();
    	while(true) {
    		System.out.println("Enter product code or Enter 0 to stop");
    		 String productCode = scanner.next();
             if (productCode.equals("0")) {
                 break;
             }
             System.out.println("Enter quantity");
             int quantity = scanner.nextInt();
             scanner.nextLine();
             Bill.BillItem billItem = billingService.createBillItem(productCode, quantity);
             billItems.add(billItem);
    	}
    
    	Bill bill = billingService.createOnlineBill(billItems, 0);
    	
    	return bill;
    }
    
    public void displayBillDetails(Bill bill) {
        System.out.println("Bill ID: " + bill.getBillId());
        System.out.println("Bill Date: " + bill.getBillDate());
        System.out.println("Total Price (Before Discount): " + (bill.getTotalPrice() + bill.getDiscount())); // Show total before discount
        System.out.println("Discount Applied: " + bill.getDiscount()); // Show discount
        System.out.println("Total Price (After Discount): " + bill.getTotalPrice());
        System.out.println("Cash Tendered: " + bill.getCashTendered());
        System.out.println("Change Amount: " + bill.getChangeAmount());

        System.out.println("Bill Items:");
        for (Bill.BillItem item : bill.getBillItems()) {
            System.out.println("  Product ID: " + item.getProductCode());
            System.out.println("  Product Name: " + item.getproductName());
            System.out.println("  Quantity: " + item.getQuantity());
            System.out.println("  Price: " + item.getPrice());
            System.out.println("  Total Price: " + item.getTotalPrice());
            System.out.println();
        }
    }
    
    
    public void displayOnlineBillDetails(Bill bill) {
        System.out.println("Bill ID: " + bill.getBillId());
        System.out.println("Bill Date: " + bill.getBillDate());
        System.out.println("Total Price:" + bill.getTotalPrice());
        System.out.println("Cash Tendered: " + bill.getCashTendered());
        System.out.println("Change Amount: " + bill.getChangeAmount());

        System.out.println("Bill Items:");
        for (Bill.BillItem item : bill.getBillItems()) {
            System.out.println("  Product ID: " + item.getProductCode());
            System.out.println("  Product Name: " + item.getproductName());
            System.out.println("  Quantity: " + item.getQuantity());
            System.out.println("  Price: " + item.getPrice());
            System.out.println("  Total Price: " + item.getTotalPrice());
            System.out.println();
        }
    }
}
