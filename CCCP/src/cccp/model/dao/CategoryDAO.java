package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cccp.database.DatabaseConnection;
import cccp.model.Category;

public class CategoryDAO{
	private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();


    // Create category using a Category object
    public int addItem(Category category) {
    	int result = 0;
        String query = "INSERT INTO categories (name) VALUES (?)";

        try (Connection connection = databaseConnection.getConnection();
        	PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, category.getName());
            int rows = pst.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setId(rs.getInt(1)); // Set the generated ID
                        System.out.println("Category created with ID: " + category.getId());
                        result = 1;
                    }
                }
            } else {
                System.out.println("Failed to create category.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			databaseConnection.closeConnection();
		}
        
        return result;
    }

    // Search category by ID and return a Category object
    public Category searchCategory(int categoryId) {
        String query = "SELECT * FROM categories WHERE id = ?";
        try (
        	Connection connection = databaseConnection.getConnection();
        	PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, categoryId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("No category found with ID: " + categoryId);
        return null;
    }

    // Display all categories
    public void viewAllItems() {
        String query = "SELECT * FROM categories";
        try (
        	Connection connection = databaseConnection.getConnection();
        	Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("All categories:");
            System.out.println("ID  |  Name");
            System.out.println("-------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(id + "  |  " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	databaseConnection.closeConnection();
        }
    }
    
    
 // Display all categories for GUI
    public List<Category> viewAllItemsGUI() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (
        	Connection connection = databaseConnection.getConnection();
        	Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                categories.add(new Category(id, name));  // Add the category to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Categories retrieved: " + categories.size()); 
        return categories;  // Return the list of categories
    }

    // Edit category using a Category object
    public void updateItem(Category category) {
        String query = "UPDATE categories SET name = ? WHERE id = ?";
        try (
        	Connection connection = databaseConnection.getConnection();
        	PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, category.getName());
            pst.setInt(2, category.getId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Category updated successfully.");
            } else {
                System.out.println("No category found with ID: " + category.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			databaseConnection.closeConnection();
		}
    }

    // Delete category by ID - 1
    public void removeItem(int categoryId) {
        String query = "DELETE FROM categories WHERE id = ?";
        try (
        	Connection connection = databaseConnection.getConnection();
        	PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, categoryId);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Category deleted successfully.");
            } else {
                System.out.println("No category found with ID: " + categoryId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	databaseConnection.closeConnection();
        }
    }
}
