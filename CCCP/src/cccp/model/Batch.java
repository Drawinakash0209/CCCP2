package cccp.model;

import java.util.Date;

public class Batch {	
	private String batchcode;
	private int quantity;
	private Date purchaseDate;
	private Date expiryDate;
	
	
	public Batch(String batchcode, int quantity, Date purchaseDate, Date expiryDate) {
	this.batchcode = batchcode;
	this.quantity = quantity;
	this.purchaseDate = purchaseDate;
	this.expiryDate = expiryDate;
	}
	
	public String getBatchcode() {
		return batchcode;
	}
	 public void setBatchcode(String batchcode) {
		this.batchcode = batchcode;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	  public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	  public Date getExpiryDate() {
		return expiryDate;
	}
	  public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	  public int getQuantity() {
		return quantity;
	}
	  public void setQuantity(int quanity) {
		this.quantity = quanity;
	}

}
