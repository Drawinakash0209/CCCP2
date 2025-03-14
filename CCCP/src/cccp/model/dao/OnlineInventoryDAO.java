package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cccp.database.DatabaseConnection;

public class OnlineInventoryDAO implements OnlineInventoryDAOInterface {
	
	// Get database connection
	  private Connection getConnection() {
	        return DatabaseConnection.getInstance().getConnection();
	    }
	
	// Add or update product quantity in inventory
	@Override
	public void addToOnlineInventory(String productId, int quantity) {
		String updateQuery = "UPDATE online_inventory SET quantity = quantity + ? WHERE product_id = ?";
	    String insertQuery = "INSERT INTO online_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)";
	    try (PreparedStatement pst = getConnection().prepareStatement(updateQuery)) {
	        pst.setInt(1, quantity);
	        pst.setString(2, productId);
	        int rows = pst.executeUpdate();

	        if (rows == 0) {  // If no rows updated, insert new record
	            try (PreparedStatement insertPst = getConnection().prepareStatement(insertQuery)) {
	                insertPst.setString(1, productId);
	                insertPst.setString(2, "DEFAULT_BATCH"); 
	                insertPst.setInt(3, quantity);
	                insertPst.executeUpdate();
	                System.out.println("New product added and restocked to online stocks.");
	            }
	        } else {
	            System.out.println("Items restocked to online stocks.");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error restocking product to online stocks: " + e.getMessage());
	        e.printStackTrace();
	    }		
	}

	// Reduce product quantity in inventory
	@Override
	public void reduceOnlineQuantity(String productCode, int quantity) {
		// TODO Auto-generated method stub
		String query = "UPDATE online_inventory SET quantity = quantity - ? WHERE product_id = ?";
		try(PreparedStatement pst = getConnection().prepareStatement(query)){
			pst.setInt(1, quantity);
			pst.setString(2, productCode);
			pst.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}
