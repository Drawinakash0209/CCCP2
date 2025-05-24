package test.dao;

import cccp.database.DatabaseConnection;
import cccp.model.DeliveryDetails;
import cccp.model.dao.DeliveryDetailsDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DeliveryDetailsDAOTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private DeliveryDetailsDAO deliveryDetailsDAO;
    private DeliveryDetails deliveryDetails;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
        deliveryDetailsDAO = new DeliveryDetailsDAO();
        Field databaseConnectionField = DeliveryDetailsDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(deliveryDetailsDAO, databaseConnection);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);
        doNothing().when(connection).close();

        deliveryDetails = new DeliveryDetails(0, 1, 100, "John Doe", "1234567890", "123 Main St");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveDeliveryDetails_Success() throws SQLException {
        when(connection.prepareStatement("INSERT INTO DeliveryDetails (bill_id, customer_id, name, phone_number, delivery_address) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        int result = deliveryDetailsDAO.saveDeliveryDetails(deliveryDetails);

        assertEquals(1, result);
        assertEquals(1, deliveryDetails.getId());
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).setInt(2, 100);
        verify(preparedStatement).setString(3, "John Doe");
        verify(preparedStatement).setString(4, "1234567890");
        verify(preparedStatement).setString(5, "123 Main St");
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
        verify(preparedStatement).close();
        verify(resultSet).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testSaveDeliveryDetails_FailedInsertion_ReturnsZero() throws SQLException {
        when(connection.prepareStatement("INSERT INTO DeliveryDetails (bill_id, customer_id, name, phone_number, delivery_address) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        int result = deliveryDetailsDAO.saveDeliveryDetails(deliveryDetails);

        assertEquals(0, result);
        assertEquals(0, deliveryDetails.getId());
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).setInt(2, 100);
        verify(preparedStatement).setString(3, "John Doe");
        verify(preparedStatement).setString(4, "1234567890");
        verify(preparedStatement).setString(5, "123 Main St");
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement, never()).getGeneratedKeys();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testSaveDeliveryDetails_SQLException_ReturnsZero() throws SQLException {
        when(connection.prepareStatement("INSERT INTO DeliveryDetails (bill_id, customer_id, name, phone_number, delivery_address) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException("Database error"));

        int result = deliveryDetailsDAO.saveDeliveryDetails(deliveryDetails);

        assertEquals(0, result);
        assertEquals(0, deliveryDetails.getId());
        verify(connection).prepareStatement("INSERT INTO DeliveryDetails (bill_id, customer_id, name, phone_number, delivery_address) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testSearchDeliveryDetails_Found() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM DeliveryDetails WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("bill_id")).thenReturn(1);
        when(resultSet.getInt("customer_id")).thenReturn(100);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("phone_number")).thenReturn("1234567890");
        when(resultSet.getString("delivery_address")).thenReturn("123 Main St");

        DeliveryDetails result = deliveryDetailsDAO.searchDeliveryDetails(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getBillId());
        assertEquals(100, result.getCustomerId());
        assertEquals("John Doe", result.getName());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("123 Main St", result.getDeliveryAddress());
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(resultSet).getInt("id");
        verify(resultSet).getInt("bill_id");
        verify(resultSet).getInt("customer_id");
        verify(resultSet).getString("name");
        verify(resultSet).getString("phone_number");
        verify(resultSet).getString("delivery_address");
        verify(preparedStatement).close();
        verify(resultSet).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testSearchDeliveryDetails_NotFound_ReturnsNull() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM DeliveryDetails WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        DeliveryDetails result = deliveryDetailsDAO.searchDeliveryDetails(1);

        assertNull(result);
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(preparedStatement).close();
        verify(resultSet).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testSearchDeliveryDetails_SQLException_ReturnsNull() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM DeliveryDetails WHERE id = ?")).thenThrow(new SQLException("Database error"));

        DeliveryDetails result = deliveryDetailsDAO.searchDeliveryDetails(1);

        assertNull(result);
        verify(connection).prepareStatement("SELECT * FROM DeliveryDetails WHERE id = ?");
        verify(preparedStatement, never()).executeQuery();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testGetAllDeliveryDetails_EmptyList() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM DeliveryDetails")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<DeliveryDetails> detailsList = deliveryDetailsDAO.getAllDeliveryDetails();

        assertTrue(detailsList.isEmpty());
        verify(statement).executeQuery("SELECT * FROM DeliveryDetails");
        verify(resultSet).next();
        verify(statement).close();
        verify(resultSet).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }


    @Test
    void testUpdateDeliveryDetails_Success() throws SQLException {
        when(connection.prepareStatement("UPDATE DeliveryDetails SET bill_id = ?, customer_id = ?, name = ?, phone_number = ?, delivery_address = ? WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        deliveryDetailsDAO.updateDeliveryDetails(new DeliveryDetails(1, 2, 200, "Jane Doe", "0987654321", "456 Elm St"));

        verify(preparedStatement).setInt(1, 2);
        verify(preparedStatement).setInt(2, 200);
        verify(preparedStatement).setString(3, "Jane Doe");
        verify(preparedStatement).setString(4, "0987654321");
        verify(preparedStatement).setString(5, "456 Elm St");
        verify(preparedStatement).setInt(6, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testUpdateDeliveryDetails_NoDetailsFound() throws SQLException {
        when(connection.prepareStatement("UPDATE DeliveryDetails SET bill_id = ?, customer_id = ?, name = ?, phone_number = ?, delivery_address = ? WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        deliveryDetailsDAO.updateDeliveryDetails(new DeliveryDetails(1, 2, 200, "Jane Doe", "0987654321", "456 Elm St"));

        verify(preparedStatement).setInt(1, 2);
        verify(preparedStatement).setInt(2, 200);
        verify(preparedStatement).setString(3, "Jane Doe");
        verify(preparedStatement).setString(4, "0987654321");
        verify(preparedStatement).setString(5, "456 Elm St");
        verify(preparedStatement).setInt(6, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testUpdateDeliveryDetails_SQLException() throws SQLException {
        when(connection.prepareStatement("UPDATE DeliveryDetails SET bill_id = ?, customer_id = ?, name = ?, phone_number = ?, delivery_address = ? WHERE id = ?")).thenThrow(new SQLException("Database error"));

        deliveryDetailsDAO.updateDeliveryDetails(new DeliveryDetails(1, 2, 200, "Jane Doe", "0987654321", "456 Elm St"));

        verify(connection).prepareStatement("UPDATE DeliveryDetails SET bill_id = ?, customer_id = ?, name = ?, phone_number = ?, delivery_address = ? WHERE id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveDeliveryDetails_Success() throws SQLException {
        when(connection.prepareStatement("DELETE FROM DeliveryDetails WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        deliveryDetailsDAO.removeDeliveryDetails(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveDeliveryDetails_NoDetailsFound() throws SQLException {
        when(connection.prepareStatement("DELETE FROM DeliveryDetails WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        deliveryDetailsDAO.removeDeliveryDetails(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveDeliveryDetails_SQLException() throws SQLException {
        when(connection.prepareStatement("DELETE FROM DeliveryDetails WHERE id = ?")).thenThrow(new SQLException("Database error"));

        deliveryDetailsDAO.removeDeliveryDetails(1);

        verify(connection).prepareStatement("DELETE FROM DeliveryDetails WHERE id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }
}