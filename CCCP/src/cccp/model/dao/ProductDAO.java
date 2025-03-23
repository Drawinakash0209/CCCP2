package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import cccp.database.DatabaseConnection;
import cccp.model.Batch;
import cccp.model.Product;

public class ProductDAO implements ProductDAOInterface{

    // Helper method to get the database connection
    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    // Creating a new product
    public void addItem(Product product) {
        String query = "INSERT INTO products (id, name, price, category_id, reorder_level) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = getConnection().prepareStatement(query)) {
        	pst.setString(1, product.getId());
            pst.setString(2, product.getName());
            pst.setDouble(3, product.getPrice());
            pst.setInt(4, product.getCategoryId());
            pst.setInt(5, product.getReorderLevel());  // Insert reorder level
            int rows = pst.executeUpdate();
            System.out.println(rows > 0 ? "Product created successfully." : "Failed to create product.");
        } catch (SQLException e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
        }
    }


 // Retrieve and display all products in a table format
    public void viewAllItems() {
        String productQuery = "SELECT * FROM products";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(productQuery)) {

            // Print table header
            System.out.println("+------------+----------------------+------------+-------------+");
            System.out.println("| Product ID | Name                 | Price      | Category ID |");
            System.out.println("+------------+----------------------+------------+-------------+");

            // Process each product
            while (rs.next()) {
                // Create Product object
                Product product = new Product.Builder()
                        .setId(rs.getString("id"))
                        .setName(rs.getString("name"))
                        .setPrice(rs.getDouble("Price"))
                        .setCategoryId(rs.getInt("Category_id"))
                        .build();

                // Print product details in table row format
                System.out.printf("| %-10s | %-20s | $%-9.2f | %-11d |%n",
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getCategoryId());
            }

            // Print table footer
            System.out.println("+------------+----------------------+------------+-------------+");
        } catch (SQLException e) {
            System.err.println("Error reading products: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<Product> viewAllItemsGUI() {
		List<Product> products = new ArrayList<>();
		String productQuery = "SELECT * FROM products";
		try (Statement stmt = getConnection().createStatement();
			 ResultSet rs = stmt.executeQuery(productQuery)) {

			while (rs.next()) {
				Product product = new Product.Builder()
						.setId(rs.getString("id"))
						.setName(rs.getString("name"))
						.setPrice(rs.getDouble("price"))
						.setReorderLevel(rs.getInt("reorder_level"))
						.setCategoryId(rs.getInt("category_id"))
						.build();
				products.add(product);
			}
		} catch (SQLException e) {
			System.err.println("Error reading products: " + e.getMessage());
			e.printStackTrace();
		}
		return products;
	}

    // Update an existing product
    public void updateItem(Product product) {
        String query = "UPDATE products SET name = ?, price = ?, category_id = ?, reorder_level = ? WHERE id = ?";

        try (PreparedStatement pst = getConnection().prepareStatement(query)) {
            pst.setString(1, product.getName());
            pst.setDouble(2, product.getPrice());
            pst.setInt(3, product.getCategoryId());
            pst.setInt(4, product.getReorderLevel());  // Update reorder level
            pst.setString(5, product.getId());
            int rows = pst.executeUpdate();
            System.out.println(rows > 0 ? "Product updated successfully." : "Failed to update product.");
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete a product by ID
    public void removeItem(String productId) {
        String query = "DELETE FROM products WHERE id = ?";

        try (PreparedStatement pst = getConnection().prepareStatement(query)) {
            pst.setString(1, productId);

            int rows = pst.executeUpdate();
            System.out.println(rows > 0 ? "Product deleted successfully." : "Failed to delete product. Product not found.");
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    public Product searchProductById(String productId) {
        String query = "SELECT * FROM products WHERE id = ?";

        try (PreparedStatement pst = getConnection().prepareStatement(query)) {
            pst.setString(1, productId);  // Using String for the ID parameter

            try (ResultSet rs = pst.executeQuery()) {
                System.out.printf("%-5s | %-20s | %-10s | %-15s\n", "ID", "Name", "Price", "Category");
                System.out.println("--------------------------------------------------------------");

                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int categoryId = rs.getInt("category_id");

                    System.out.printf("%-5s | %-20s | %-10.2f | %-15d\n", id, name, price, categoryId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching product: " + e.getMessage());
            e.printStackTrace();
        }
        return null;  // Return null if no product is found with the given ID
    }
    
    
    public Product getProductById(String productId) {
        String query = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(query)) {
            pst.setString(1, productId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Product.Builder()
                            .setId(rs.getString("id"))
                            .setName(rs.getString("name"))
                            .setPrice(rs.getDouble("price"))
                            .setCategoryId(rs.getInt("category_id"))
                            .build();
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving product by ID: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Error preparing statement: " + e.getMessage());
            e.printStackTrace();
        }
        return null;  // Return null if no product is found with the given ID
    }
    
   
    
 // Update the quantity of a product in the database
    public void updateProductQuantity(String productId, int quantity) {
        String query = "UPDATE products SET quantity = ? WHERE id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(query)) {
            pst.setInt(1, quantity);
            pst.setString(2, productId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //products that need to be reordered (quantity < 50)
    public List<Product> getProductBelowReorderLevel(){
    	List<Product> products = new ArrayList<>();
    	String query = "SELECT * FROM products WHERE quantity < ?";
    	try (PreparedStatement pst = getConnection().prepareStatement(query)) {
    		pst.setInt(1, 50);
    		try(ResultSet rs = pst.executeQuery()){
    			while(rs.next()) {
    				String id = rs.getString("id");
    				String name = rs.getString("name");
    				double price = rs.getDouble("price");
    				int quantity = rs.getInt("quantity");
    				
    				Product product = new Product.Builder()
    						.setId(id)
    						.setName(name)
    						.setPrice(price)
    						.setQuantity(quantity)
    						.build();
    				products.add(product);
    			}
    		}
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
		return products;
    	
    }
    
 // Stock report generation: Batch-wise stock details
    public void generateStockReport() {
        String query = "SELECT p.id AS product_id, p.name AS product_name, " +
                       "b.batch_code, b.quantity, b.purchase_date, b.expiry_date " +
                       "FROM products p " +
                       "INNER JOIN batches b ON p.id = b.product_id " +
                       "ORDER BY p.id, b.batch_code";

        try (PreparedStatement pst = getConnection().prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            System.out.printf("%-10s | %-20s | %-10s | %-10s | %-15s | %-15s\n",
                              "Product ID", "Product Name", "Batch Code", "Quantity", "Purchase Date", "Expiry Date");
            System.out.println("--------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String productId = rs.getString("product_id");
                String productName = rs.getString("product_name");
                String batchCode = rs.getString("batch_code");
                int quantity = rs.getInt("quantity");
                String purchaseDate = rs.getString("purchase_date");
                String expiryDate = rs.getString("expiry_date");

                System.out.printf("%-10s | %-20s | %-10s | %-10d | %-15s | %-15s\n",
                                  productId, productName, batchCode, quantity, purchaseDate, expiryDate);
            }
        } catch (SQLException e) {
            System.err.println("Error generating stock report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    public List<Product> getAllProducts(){
    	List<Product> products = new ArrayList<>();
    	String query = "SELECT id, name, reorder_level, price FROM products";
    	
    	 try (PreparedStatement pst = getConnection().prepareStatement(query);
                 ResultSet rs = pst.executeQuery()) {
    		 while(rs.next()) {
    			String id = rs.getString("id");
 				String name = rs.getString("name");
 				int restockQuantity = rs.getInt("reorder_level");
 				double price = rs.getDouble("price");
 				
 				
    			 Product product = new Product.Builder()
 						.setId(id)
 						.setName(name)
 						.setPrice(price)
 						.setReorderLevel(restockQuantity)
 						.build();
    			 products.add(product);
    		 }
    		 
    	 }catch(SQLException e) {
    		 e.printStackTrace();
    	 }
    	 
    	 return products;
    }

    

	




}
