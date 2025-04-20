package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import cccp.database.DatabaseConnection;
import cccp.model.Batch;


public class BatchDAO implements BatchDAOInterface {
	private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
	 	 
	 public void addBatch(Batch batch, String productId) {
	        String query = "INSERT INTO batches (product_id, batch_code, quantity, purchase_date, expiry_date) VALUES (?, ?, ?, ?, ?)";
	        try (
	        	Connection connection = databaseConnection.getConnection();
	        	PreparedStatement pst = connection.prepareStatement(query)) {
	            pst.setString(1, productId);
	            pst.setString(2, batch.getBatchcode());
	            pst.setInt(3, batch.getQuantity());
	            pst.setDate(4, new java.sql.Date(batch.getPurchaseDate().getTime()));
	            pst.setDate(5, new java.sql.Date(batch.getExpiryDate().getTime()));

	            int rows = pst.executeUpdate();
	            if (rows > 0) {
	                System.out.println("Batch added successfully.");
	            } else {
	                System.err.println("Failed to add batch.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	        	databaseConnection.closeConnection();
	        }
	    }

	 
	 
	 public void updateBatch(Batch batch) {
		 String query = "UPDATE batches SET quantity = ?, purchase_date = ?, expiry_date = ? WHERE batch_code = ?"; 
		 try (
			 Connection connection = databaseConnection.getConnection();	
			PreparedStatement pst = connection.prepareStatement(query)) { 
			 pst.setInt(1, batch.getQuantity()); 
			 pst.setDate(2, new java.sql.Date(batch.getPurchaseDate().getTime())); 
			 pst.setDate(3, new java.sql.Date(batch.getExpiryDate().getTime())); 
			 pst.setString(4, batch.getBatchcode()); 
			 int rows = pst.executeUpdate(); 
			 System.out.println(rows > 0 ? "Batch updated successfully." : "Failed to update batch."); 
			 } 
		 catch (SQLException e) 
		 { System.err.println("Error updating batch: " + e.getMessage()); e.printStackTrace(); } 
		 finally { 
			 databaseConnection.closeConnection(); 
			 }
		 }
	 
	 
	 public void updateBatchQuantity(Batch batch) {
	        String query = "UPDATE batches SET quantity = ? WHERE batch_code = ?";
	        try (
	        	Connection connection = databaseConnection.getConnection();
	        	PreparedStatement pst = connection.prepareStatement(query)) {
	        	pst.setInt(1, batch.getQuantity());
	        	pst.setString(2, batch.getBatchcode());
	        	pst.executeUpdate();
	        } catch (SQLException e) {
	            throw new RuntimeException("Error updating batch quantity", e);
	        } finally {
	        	databaseConnection.closeConnection();
	        }
	    }
	 

	 public List<Batch> getBatchesByProductId(String productId){
		 List<Batch> batches = new ArrayList<>();
		 String query = "SELECT * FROM batches WHERE product_id = ?";
		 try(
			 Connection connection = databaseConnection.getConnection();
			 PreparedStatement pst = connection.prepareStatement(query)){
			 pst.setString(1, productId);
			 try(ResultSet rs = pst.executeQuery()){
				 while(rs.next()) {
					 String batchcode = rs.getString("batch_code");
					 int quantity =rs.getInt("quantity");
					 Date purchaseDate = rs.getDate("purchase_date");
					 Date expiryDate = rs.getDate("expiry_date");
					 Batch batch = new Batch(batchcode, quantity, purchaseDate, expiryDate);
					 batches.add(batch);
				 }
			 }catch(SQLException e){
				 System.out.println("Error reading batches: " + e.getMessage());
				 e.printStackTrace();			 
			 }
			
		 }catch(SQLException e){
			 System.out.println("Error preparing statement: " + e.getMessage());
			 e.printStackTrace();
			}
		 
		 return batches;				 
		 
	 }
	 
	 
	   // New method to calculate the total quantity of all batches for a given product
	    public int getTotalQuantityForProduct(String productId) {
	        String query = "SELECT SUM(quantity) AS total_quantity FROM batches WHERE product_id = ?";
	        try (
	        	Connection connection = databaseConnection.getConnection();
	        	PreparedStatement pst = connection.prepareStatement(query)) {
	            pst.setString(1, productId);
	            ResultSet rs = pst.executeQuery();
	            if (rs.next()) {
	                return rs.getInt("total_quantity");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	        	databaseConnection.closeConnection();
	        }
	        return 0; 
	    }
	 
	 


}
