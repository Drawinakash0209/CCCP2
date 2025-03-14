package cccp;

import java.util.List;

import cccp.model.Bill;

public class BillReport {
	private List<Bill> bills;
	
	public BillReport(List<Bill> bills) {
		this.bills = bills;
	}
	
	public void display() {
		System.out.println("=====BILL REPORT====");
		for(Bill bill:bills) {
			System.out.println("Bill ID " + bill.getBillId());
			System.out.println("Bill Date " + bill.getBillDate());
            System.out.println("\n---------------------------------------------------------------");
            System.out.printf("%-20s %-10s %-12s %-10s%n", "Product Name", "Quantity", "Unit Price", "Total Price");
            System.out.println("---------------------------------------------------------------");
            for(Bill.BillItem product : bill.getBillItems()) {
            	System.out.printf("%-20s %-10d %-12.2f %-10.2f%n", product.getproductName(), product.getQuantity(), product.getPrice(), product.getTotalPrice());
            }
            System.out.println("---------------------------------------------------------------\n");
            System.out.printf("%-42s %-10.2f%n", "Total Price:", bill.getTotalPrice());
            System.out.printf("%-42s %-10.2f%n", "Cash Tendered:", bill.getCashTendered());
            System.out.printf("%-42s %-10.2f%n", "Change Amount:", bill.getChangeAmount());
            System.out.println("===============================================================\n\n");
			
		}
	}
	


}
