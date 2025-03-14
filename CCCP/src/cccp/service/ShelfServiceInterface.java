package cccp.service;

import java.util.Date;

import cccp.ShelfRestockListener;
import cccp.strategy.BatchSelectionStrategy;

public interface ShelfServiceInterface {

	 // Method to add a restock listener
    void addRestockListener(ShelfRestockListener listener);

    // Method to restock a shelf by adding batches of a product
    void restockShelf(String productId, int quantity, Date currentDate, BatchSelectionStrategy strategy);

    // Method to reduce the quantity of a product on the shelf
    void reduceShelf(String productCode, int quantity);
}
