package test.dao;

import cccp.database.DatabaseConnection;
import cccp.model.dao.OnlineInventoryDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OnlineInventoryDAOTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement updateStatement;

    @Mock
    private PreparedStatement insertStatement;

    private OnlineInventoryDAO onlineInventoryDAO;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
        onlineInventoryDAO = new OnlineInventoryDAO();
        Field databaseConnectionField = OnlineInventoryDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(onlineInventoryDAO, databaseConnection);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);
        doNothing().when(connection).close();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddToOnlineInventory_UpdateSuccess() throws SQLException {
        when(connection.prepareStatement("UPDATE online_inventory SET quantity = quantity + ? WHERE product_id = ?")).thenReturn(updateStatement);
        when(updateStatement.executeUpdate()).thenReturn(1);

        onlineInventoryDAO.addToOnlineInventory("P001", 10);

        verify(updateStatement).setInt(1, 10);
        verify(updateStatement).setString(2, "P001");
        verify(updateStatement).executeUpdate();
        verify(updateStatement).close();
        verify(connection, never()).prepareStatement("INSERT INTO online_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)");
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testAddToOnlineInventory_InsertSuccess() throws SQLException {
        when(connection.prepareStatement("UPDATE online_inventory SET quantity = quantity + ? WHERE product_id = ?")).thenReturn(updateStatement);
        when(updateStatement.executeUpdate()).thenReturn(0);
        when(connection.prepareStatement("INSERT INTO online_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)")).thenReturn(insertStatement);
        when(insertStatement.executeUpdate()).thenReturn(1);

        onlineInventoryDAO.addToOnlineInventory("P001", 10);

        verify(updateStatement).setInt(1, 10);
        verify(updateStatement).setString(2, "P001");
        verify(updateStatement).executeUpdate();
        verify(updateStatement).close();
        verify(insertStatement).setString(1, "P001");
        verify(insertStatement).setString(2, "DEFAULT_BATCH");
        verify(insertStatement).setInt(3, 10);
        verify(insertStatement).executeUpdate();
        verify(insertStatement).close();
        verify(databaseConnection, times(2)).closeConnection();
        verify(connection, times(2)).close();
    }

    @Test
    void testAddToOnlineInventory_UpdateSQLException() throws SQLException {
        when(connection.prepareStatement("UPDATE online_inventory SET quantity = quantity + ? WHERE product_id = ?")).thenThrow(new SQLException("Database error"));

        onlineInventoryDAO.addToOnlineInventory("P001", 10);

        verify(connection).prepareStatement("UPDATE online_inventory SET quantity = quantity + ? WHERE product_id = ?");
        verify(updateStatement, never()).executeUpdate();
        verify(connection, never()).prepareStatement("INSERT INTO online_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)");
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testAddToOnlineInventory_InsertSQLException() throws SQLException {
        when(connection.prepareStatement("UPDATE online_inventory SET quantity = quantity + ? WHERE product_id = ?")).thenReturn(updateStatement);
        when(updateStatement.executeUpdate()).thenReturn(0);
        when(connection.prepareStatement("INSERT INTO online_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)")).thenThrow(new SQLException("Database error"));

        onlineInventoryDAO.addToOnlineInventory("P001", 10);

        verify(updateStatement).setInt(1, 10);
        verify(updateStatement).setString(2, "P001");
        verify(updateStatement).executeUpdate();
        verify(updateStatement).close();
        verify(connection).prepareStatement("INSERT INTO online_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)");
        verify(insertStatement, never()).executeUpdate();
        verify(databaseConnection, times(2)).closeConnection();
        verify(connection, times(2)).close();
    }


 
}