package cccp.service;

import cccp.model.Batch;

public interface ProductServiceInterface {
	
	 // Method to add a batch of products to a specific product
    void addBatchToProduct(String productId, Batch batch);

    // Method to recalculate the total quantity for a product
    void recalculateProductQuantity(String productId);

    // Method to handle shelf restocked notification (comes from ShelfRestockListener)
    void onShelfRestocked(String productId);

}
