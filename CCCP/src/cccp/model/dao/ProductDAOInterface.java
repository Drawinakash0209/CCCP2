package cccp.model.dao;

import java.util.List;

import cccp.model.Product;

public interface ProductDAOInterface {
	  // Method to add a new product
    void addItem(Product product);

    // Method to view all products
    void viewAllItems();

    // Method to update an existing product
    void updateItem(Product product);

    // Method to delete a product by ID
    void removeItem(String productId);

    // Method to search a product by its ID
    Product searchProductById(String productId);
    
    // Method to retrieve product by ID (returns a Product object)
    Product getProductById(String productId);

    // Method to update the quantity of a product
    void updateProductQuantity(String productId, int quantity);

    // Method to get all products below the reorder level
    List<Product> getProductBelowReorderLevel();

    // Method to generate stock report
    List<StockItem> generateStockReport();
    
    // Method to get all products
    List<Product> getAllProducts();
}
