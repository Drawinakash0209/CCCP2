package cccp;

import java.util.List;

public class ReshelveReport {
	private List<Item> items;
	
	public ReshelveReport(List<Item> items) {
		this.items = items;
	}
	
	public List<Item> getItems(){
		return items;
	}
	
	public static class Item{
		private String productId;
		private String productName;
		private int quantityToReshelve;
		
		public Item(String productId, String productName, int quantityToReshelve) {
			this.productId = productId;
			this.productName = productName;
			this.quantityToReshelve = quantityToReshelve;
		}
		
		public String getProductId() {
			return productId;
		}
		public String getProductName() {
            return productName;
        }
		
		public int getQuantityToReshelve() {
            return quantityToReshelve;
        }
		
		
	}

}
