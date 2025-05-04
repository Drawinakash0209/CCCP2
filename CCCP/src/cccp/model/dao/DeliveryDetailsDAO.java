package cccp.model.dao;

import cccp.database.DatabaseConnection;
import cccp.model.DeliveryDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDetailsDAO implements DeliveryDetailsDAOInterface {
    private final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    @Override
    public int saveDeliveryDetails(DeliveryDetails details) {
        int result = 0;
        String query = "INSERT INTO DeliveryDetails (bill_id, customer_id, name, phone_number, delivery_address) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, details.getBillId());
            pstmt.setInt(2, details.getCustomerId());
            pstmt.setString(3, details.getName());
            pstmt.setString(4, details.getPhoneNumber());
            pstmt.setString(5, details.getDeliveryAddress());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        details.setId(rs.getInt(1)); // Set the generated ID
                        System.out.println("Delivery details created with ID: " + details.getId());
                        result = 1;
                    }
                }
            } else {
                System.out.println("Failed to save delivery details.");
            }
        } catch (SQLException e) {
            System.err.println("Error saving delivery details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            databaseConnection.closeConnection();
        }

        return result;
    }

    // Search delivery details by ID
    public DeliveryDetails searchDeliveryDetails(int id) {
        String query = "SELECT * FROM DeliveryDetails WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DeliveryDetails(
                        rs.getInt("id"),
                        rs.getInt("bill_id"),
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("phone_number"),
                        rs.getString("delivery_address")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching delivery details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            databaseConnection.closeConnection();
        }
        System.out.println("No delivery details found with ID: " + id);
        return null;
    }

    // Retrieve all delivery details (for potential admin use)
    public List<DeliveryDetails> getAllDeliveryDetails() {
        List<DeliveryDetails> detailsList = new ArrayList<>();
        String query = "SELECT * FROM DeliveryDetails";
        try (Connection connection = databaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                DeliveryDetails details = new DeliveryDetails(
                    rs.getInt("id"),
                    rs.getInt("bill_id"),
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getString("delivery_address")
                );
                detailsList.add(details);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving delivery details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            databaseConnection.closeConnection();
        }
        System.out.println("Delivery details retrieved: " + detailsList.size());
        return detailsList;
    }

    // Update delivery details
    public void updateDeliveryDetails(DeliveryDetails details) {
        String query = "UPDATE DeliveryDetails SET bill_id = ?, customer_id = ?, name = ?, phone_number = ?, delivery_address = ? WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, details.getBillId());
            pstmt.setInt(2, details.getCustomerId());
            pstmt.setString(3, details.getName());
            pstmt.setString(4, details.getPhoneNumber());
            pstmt.setString(5, details.getDeliveryAddress());
            pstmt.setInt(6, details.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Delivery details updated successfully.");
            } else {
                System.out.println("No delivery details found with ID: " + details.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating delivery details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            databaseConnection.closeConnection();
        }
    }

    // Delete delivery details by ID
    public void removeDeliveryDetails(int id) {
        String query = "DELETE FROM DeliveryDetails WHERE id = ?";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Delivery details deleted successfully.");
            } else {
                System.out.println("No delivery details found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting delivery details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            databaseConnection.closeConnection();
        }
    }
}