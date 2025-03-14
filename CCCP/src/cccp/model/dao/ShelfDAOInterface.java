package cccp.model.dao;

public interface ShelfDAOInterface {
	 // Retrieves the quantity of a product available on the shelf
    int getshelfQuantityByProduct(String productId);
    
    // Adds a specified quantity of a product to the shelf
    void addToShelf(String productId, int quantity);
    
    // Reduces the quantity of a product from the shelf
    void reduceShelfQuantity(String productCode, int quantity);
	
}
