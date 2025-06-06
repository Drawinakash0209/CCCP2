package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cccp.database.DatabaseConnection;
import cccp.model.Bill;
import cccp.model.Bill.BillItem;

public class BillDAO implements BillDAOInterface {
		private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    
    public void addBill(Bill bill) {
        String insertBillQuery = "INSERT INTO Bill (bill_id, bill_date, total_price, cash_tendered, change_amount) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;

        try {
        	Connection connection = databaseConnection.getConnection();
            pstmt = connection.prepareStatement(insertBillQuery);
            pstmt.setInt(1, bill.getBillId());
            pstmt.setDate(2, new java.sql.Date(bill.getBillDate().getTime()));
            pstmt.setDouble(3, bill.getTotalPrice());
            pstmt.setDouble(4, bill.getCashTendered());
            pstmt.setDouble(5, bill.getChangeAmount());

            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Bill saved successfully." : "Failed to save bill.");

            saveBillItems(bill.getBillId(), bill.getBillItems());
        } catch (SQLException e) {
            System.err.println("Error saving bill: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
            databaseConnection.closeConnection();
        }
    }
     
    private void saveBillItems(int billId, List<Bill.BillItem> billItems) {
        String insertBillItemQuery = "INSERT INTO BillItems (bill_id, item_code, quantity, total_price) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;

        try {
        	Connection connection = databaseConnection.getConnection();
            pstmt = connection.prepareStatement(insertBillItemQuery);
            for (Bill.BillItem item : billItems) {
                pstmt.setInt(1, billId);
                pstmt.setString(2, item.getProductCode());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setDouble(4, item.getTotalPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("Bill items saved successfully.");
        } catch (SQLException e) {
            System.err.println("Error saving bill items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
            
            databaseConnection.closeConnection();
        }
    }

	
    public int getNextBillId() {
        String query = "SELECT MAX(bill_id) AS max_id FROM Bill";

        try (
        	Connection connection = databaseConnection.getConnection();
        	PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return rs.wasNull() ? 1 : maxId + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching next bill ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
			databaseConnection.closeConnection();
		}
        return 1; // Default to 1 if query fails or no bills exist
    }
    
    
    public List<Bill> generateAllBillReports() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bill";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int billId = rs.getInt("bill_Id");

                // Now pass the same connection
                List<Bill.BillItem> billItems = getBillItemsById(billId, connection);

                Bill.BillBuilder billBuilder = new Bill.BillBuilder()
                    .setBillId(billId)
                    .setBillDate(rs.getDate("bill_date"))
                    .setTotalPrice(rs.getDouble("total_price"))
                    .setCashTendered(rs.getDouble("cash_tendered"))
                    .setchangeAmount(rs.getDouble("change_amount"));

                for (Bill.BillItem item : billItems) {
                    billBuilder.addBillItem(item);
                }

                bills.add(billBuilder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            databaseConnection.closeConnection();
        }

        return bills;
    }

    
    public List<Bill.BillItem> getBillItemsById(int billId, Connection connection) {
        List<Bill.BillItem> billItems = new ArrayList<>();
        String query = "SELECT bi.*, p.name, p.price " +
                       "FROM billItems bi " +
                       "JOIN products p ON bi.item_code = p.id " +
                       "WHERE bi.bill_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, billId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Bill.BillItem item = Bill.BillItemFactory.createBillItem(
                        rs.getString("item_code"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("total_price")
                    );
                    billItems.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return billItems;
    }

	@Override
	public List<BillItem> getBillItemsById(int billId) {
		List<Bill.BillItem> billItems = new ArrayList<>();
    	String query = "SELECT bi.*, p.name, p.price "+
    			"FROM billItems bi " + 
    			"JOIN products p ON bi.item_code = p.id " + 
    			"WHERE bi.bill_id = ? ";
    	try(
    		Connection connection = databaseConnection.getConnection();
    		PreparedStatement pstmt = connection.prepareStatement(query)){
    		pstmt.setInt(1, billId);
    		try(ResultSet rs = pstmt.executeQuery()){
    			while(rs.next()) {
    				Bill.BillItem billItem = Bill.BillItemFactory.createBillItem(
    						rs.getString("item_code"),
    						rs.getString("name"),
    						rs.getInt("quantity"),
    						rs.getDouble("price"),
    						rs.getDouble("total_price")
    						);
    				billItems.add(billItem);	
    			}
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	} 
    	return billItems;
	}

    
    
}
