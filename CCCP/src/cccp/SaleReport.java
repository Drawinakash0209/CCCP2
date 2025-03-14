package cccp;

import java.util.Map;

public class SaleReport {
	private final Map <String, Summary> summary;
	
	public SaleReport(Map<String, Summary> summary) {
		this.summary = summary;
	}
	
	public void display() {
		System.out.println("=====DAILY SALES REPORT=====");
		System.out.println("---------------------------------");
        System.out.println(String.format("%-20s %-10s %-10s %-10s", "product Name", "product Code", "Quantity", "Revenue"));
        System.out.println("-------------------------------------------------------------");
        for (Summary summary: summary.values()) {
        	System.out.println(String.format("%-20s %-10s %-10d %-10.2f",
        			summary.getProductCode(), summary.getProductName(), summary.getTotalQuantity(), summary.getTotalRevenue()));
        }
        System.out.println("-------------------------------------------------------------");
	}
	




public static class Summary{
	private final String productCode;
	private final String productName;
	private int totalQuantity;
	private double totalRevenue;
	
	public Summary(String productCode,String productName) {
		this.productCode = productCode;
		this.productName = productName;
		this.totalQuantity = 0;
		this.totalRevenue = 0.0;
		
	}
	
	public void addQuantity(int quantity) {
		this.totalQuantity += quantity;
	}
	
	public void addRevenue(double revenue) {
		this.totalRevenue += revenue;
	}
	
	public String getProductCode() {
		return productCode;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public int getTotalQuantity() {
		return totalQuantity;
	}
	
	public double getTotalRevenue() {
		return totalRevenue;
	}
	
}
}