package cccp.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
	private final String id;
	private final String name;
	private final double price;
	private final int categoryId;
	private final List<Batch> batches;
    private int quantity;  // Total quantity of the product in stock
    private int reorderLevel;  // The reorder level for the product
	
	
	public Product(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.price = builder.price;
		this.categoryId = builder.categoryId;
		this.batches = builder.batches == null ? new ArrayList<>() : builder.batches;	
		this.quantity = calculateTotalQuantity(); // Calculate the total quantity from all batches
        this.reorderLevel = builder.reorderLevel;
	}
	
	public static class Builder {
		private String id;
		private String name;
		private double price;
		private int categoryId;
		private List<Batch> batches;
		private int reorderLevel;
		private int quantity;
		
		public Builder setId(String id) {
			this.id = id;
			return this;
		}
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder setPrice(double price) {
		    this.price = price;
		    return this;
		}
		
		public Builder setCategoryId(int categoryId){
			this.categoryId = categoryId;
			return this;
		}
		
		public Builder setBatches(List<Batch> batches) {
			this.batches = batches != null ? new ArrayList<>(batches) : null;
			return this;
		}
		  
		public Builder setReorderLevel(int reorderLevel) {
		    this.reorderLevel = reorderLevel;
		    return this;
		}

		public Builder setQuantity(int quantity) {
		    this.quantity = quantity;
		    return this;
		}
		
		public Product build() {
			return new Product(this);
		}
	}
	
	
	// GETTERS
	
	public String getId(){
		return id;
	}
		
	public String getName(){
		return name;
	}
	
	public double getPrice(){
		return price;
	}


	public int getCategoryId(){
		return categoryId;
	}
	
	public List<Batch> getBatches(){
		return batches;
	}
	
	public int getQuantity() {
		return quantity;
	}

	    public int getReorderLevel() {
	        return reorderLevel;
	    }
	
	
	//Manage batches
	    public void addBatch(Batch batch) { 
	        this.batches.add(batch);
	        this.quantity = calculateTotalQuantity();  // Recalculate the total quantity of the product
	    }

	
//	public void removeBatch(Batch batch){ 
//		this.batches.remove(batch); 
//	}
	
	 // Calculate total quantity across all batches
    private int calculateTotalQuantity() {
        return batches.stream().mapToInt(Batch::getQuantity).sum();
    }	

}
