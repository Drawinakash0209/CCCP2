package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cccp.database.DatabaseConnection;
import cccp.model.Sale;

public class SaleDAO implements SaleDAOInterface{
	private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }


	public void addSale(Sale sale) {
		String query = "INSERT INTO sales (product_code, quantity_sold, total_revenue, sales_date, sale_type) VALUES (?,?,?,?,?)";
		try(PreparedStatement pst = getConnection().prepareStatement(query)){
			pst.setString(1, sale.getProductCode());
			pst.setInt(2, sale.getQuantitySold());
			pst.setDouble(3, sale.getTotalRevenue());
			pst.setDate(4, new java.sql.Date(sale.getSalesDate().getTime()));
			pst.setString(5, sale.getSaleType());
			pst.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	public List<Sale> getSalesByDate(Date date) {
	    List<Sale> sales = new ArrayList<>();
	    String query = "SELECT * FROM sales WHERE sales_date = ?";
	    try (PreparedStatement pst = getConnection().prepareStatement(query)) {
	        pst.setDate(1, new java.sql.Date(date.getTime())); 
	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                Sale sale = getSaleFromResults(rs);  
	                sales.add(sale);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return sales;
	}

	
	private Sale getSaleFromResults(ResultSet rs) throws SQLException {
		return new Sale.SaleBuilder()
				.setSaleId(rs.getInt("sale_id"))
				.setProductCode(rs.getString("product_code"))
				.setQuantitySold(rs.getInt("quantity_sold"))
				.setTotalRevenue(rs.getDouble("total_revenue"))
				.setSalesDate(rs.getDate("sales_date"))
				.build();
	}

}
