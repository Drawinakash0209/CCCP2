package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cccp.database.DatabaseConnection;

public class ShelfDAO implements ShelfDAOInterface {
	
	private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }
	
	public int getshelfQuantityByProduct(String productId) {
		int shelfQuantity = 0;
		String query = "SELECT quantity FROM shelf_inventory WHERE product_id = ?";
		
		try (PreparedStatement pst = getConnection().prepareStatement(query)) {
	        pst.setString(1, productId);
	        ResultSet rs = pst.executeQuery();
	        
	        if(rs.next()) {
	        	shelfQuantity = rs.getInt("quantity");
	        }
	        
	}catch(SQLException e) {
		e.printStackTrace();
	}
		return shelfQuantity;
	}
	
	
	@Override
	public void addToShelf(String productId, int quantity) {
	    String updateQuery = "UPDATE shelf_inventory SET quantity = quantity + ? WHERE product_id = ?";
	    String insertQuery = "INSERT INTO shelf_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)";
	    try (PreparedStatement pst = getConnection().prepareStatement(updateQuery)) {
	        pst.setInt(1, quantity);
	        pst.setString(2, productId);
	        int rows = pst.executeUpdate();

	        if (rows == 0) {  // If no rows updated, insert new record
	            try (PreparedStatement insertPst = getConnection().prepareStatement(insertQuery)) {
	                insertPst.setString(1, productId);
	                insertPst.setString(2, "DEFAULT_BATCH");  // Set a default batch code
	                insertPst.setInt(3, quantity);
	                insertPst.executeUpdate();
	                System.out.println("New product added and restocked to shelves.");
	            }
	        } else {
	            System.out.println("Items restocked to shelves.");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error restocking product to shelves: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	@Override
	public void reduceShelfQuantity(String productCode, int quantity) {
		// TODO Auto-generated method stub
		String query = "UPDATE shelf_inventory SET quantity = quantity - ? WHERE product_id = ?";
		try(PreparedStatement pst = getConnection().prepareStatement(query)){
			pst.setInt(1, quantity);
			pst.setString(2, productCode);
			pst.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

}
