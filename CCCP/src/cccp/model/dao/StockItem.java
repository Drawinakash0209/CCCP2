package cccp.model.dao;

public class StockItem {
	private String productId;
	private String productName;
	private String batchId;
	private int quantity;
	private String purchaseDate;
	private String expiryDate;
	
	public StockItem(String productId, String productName, String batchId, int quantity, String purchaseDate,
			String expiryDate) {
		this.productId = productId;
		this.productName = productName;
		this.batchId = batchId;
		this.quantity = quantity;
		this.purchaseDate = purchaseDate;
		this.expiryDate = expiryDate;
	}
	
	//getters and setters
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getbatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	

}
