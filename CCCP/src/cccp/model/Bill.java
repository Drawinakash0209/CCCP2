package cccp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bill {
	private int billId;
	private Date billDate;
	private double totalPrice;
	private double cashTendered;
	private double changeAmount;
	private double discount;
	private List<BillItem> billItems;
	
	private Bill(BillBuilder builder) {
		this.billId = builder.billId;
	    this.billDate = builder.billDate;
	    this.totalPrice = builder.totalPrice;
	    this.cashTendered = builder.cashTendered;
	    this.changeAmount = builder.changeAmount;
	    this.discount = builder.discount;
	    this.billItems = builder.billItems;
		
	}
	
	public static class BillBuilder{
		private int billId;
		private Date billDate;
		private double totalPrice;
		private double cashTendered;
		private double changeAmount;
		private double discount;
		private List<BillItem> billItems = new ArrayList<>();
		
		
		public BillBuilder setBillId(int billId) {
			this.billId = billId;
			return this;
		}
		
		public BillBuilder setBillDate(Date billDate) {
			this.billDate = billDate;
			return this;
		}
		
		public BillBuilder setTotalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
			return this;
		}
		
		public BillBuilder setCashTendered(double cashTendered) {
			this.cashTendered = cashTendered;
			return this;
		}
		
		public BillBuilder setchangeAmount(double changeAmount) {
			this.changeAmount = changeAmount;
			return this;
		}
		
		public BillBuilder setDiscount(double discount) {
			this.discount = discount;
			return this;
		}
		
		public BillBuilder addBillItem(BillItem billItem) {
			this.billItems.add(billItem);
			return this;
		}
		
		public Bill build() {
			return new Bill(this);
		}
	}
	
	
	public static class BillItemFactory{
		public static BillItem createBillItem(String productId, String productName, int quantity, double price, double totalPrice) {
            return new BillItem(productId, productName, quantity, price, totalPrice);
            }
		}
	
	public static class BillItem{
		private String productId;
		private String productName;
		private int quantity;
		private double price;
		private double totalPrice;
		
		private BillItem(String productId, String productName, int quantity, double price, double totalPrice) {
			this.productId = productId;
			this.productName = productName;
			this.quantity = quantity;
			this.price = price;
			this.totalPrice = totalPrice; 
		}
		
		
		public String getProductCode()
		{
			return productId;
		}
		
		public void setProductCode(String productId) {
			this.productId = productId;
		}
		
		public String getproductName() {
			return productName;
		}
		
		public void setProductName(String productName) {
			this.productName = productName;
		}
		
		public int getQuantity() {
			return quantity;
		}
		
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		
		public double getPrice() {
			return price;
		}
		
		public void setPrice(double price) {
			this.price = price;
		}
		
		
		public double getTotalPrice() {
			return totalPrice;
		}
		
		public void setTotalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
		}
	}
	
	public int getBillId() {
		return billId;
	}
	
	public Date getBillDate() {
		return billDate;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	
	public double getCashTendered() {
		return cashTendered;
	}
	
	public double getChangeAmount() {
		return changeAmount;
	}
	
	 public double getDiscount() {
	        return discount;
	 }
	
	public List<BillItem> getBillItems(){
		return billItems;
	}
	
	
}
