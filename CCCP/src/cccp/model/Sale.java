package cccp.model;

import java.util.Date;

public class Sale {
	private int saleId;
	private String productCode;
	private int quantitySold;
	private double totalRevenue;
	private Date salesDate;
	private String saleType;
	
	public Sale(SaleBuilder builder) {
		this.saleId = builder.saleId;
		this.productCode = builder.productCode;
		this.quantitySold = builder.quantitySold;
		this.totalRevenue = builder.totalRevenue;
		this.salesDate = builder.salesDate;
		this.saleType = builder.saleType;
	}
	
	public static class SaleBuilder{
		private int saleId;
		private String productCode;
		private int quantitySold;
		private double totalRevenue;
		private Date salesDate;
		private String saleType;
		
		public SaleBuilder setSaleId(int saleId) {
			this.saleId = saleId;
			return this;
		}
		
		public SaleBuilder setProductCode(String productCode) {
		    if (productCode == null || productCode.isEmpty()) {
		        throw new IllegalArgumentException("productCode is required");
		    }
		    this.productCode = productCode;
		    return this;
		}
		
		public SaleBuilder setQuantitySold(int quantitySold) {
		    if (quantitySold <= 0) {
		        throw new IllegalArgumentException("quantitySold must be positive");
		    }
		    this.quantitySold = quantitySold;
		    return this;
		}
		
		public SaleBuilder setTotalRevenue(double totalRevenue) {
			this.totalRevenue = totalRevenue;
			return this;
		}
		
		public SaleBuilder setSalesDate(Date salesDate) {
			this.salesDate = salesDate;
			return this;
		}
		
		public SaleBuilder setSaleType(String saleType) {
            this.saleType = saleType;
            return this;
        }
		
		public Sale build() {
			return new Sale(this);
		}
		
		
	}
	
	public static Sale saleCreation(int saleId, String productCode, int quantitySold, double totalRevenue, Date salesDate, String saleType) {
		return new Sale.SaleBuilder()
				.setSaleId(saleId)
				.setProductCode(productCode)
				.setQuantitySold(quantitySold)
				.setTotalRevenue(totalRevenue)
				.setSalesDate(salesDate)
				.setSaleType(saleType)
				.build();
	}
	
	public int getSalesId() {
		return saleId;
	}
	
	public void setSaleId(int saleID) {
		this.saleId = saleID; 
	}
	
	public String getProductCode() {
		return productCode;		
	}
	
	public void setProductCode(String productCode ) {
		this.productCode = productCode;
	}
	
	public int getQuantitySold() {
		return quantitySold;
	}
	
	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}
	
	public double getTotalRevenue() {
		return totalRevenue;
	}
	
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	
	public Date getSalesDate() {
		return salesDate;
	}
	
	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}
	
	public String getSaleType() {
		return saleType;
	}
	
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	
	

}
