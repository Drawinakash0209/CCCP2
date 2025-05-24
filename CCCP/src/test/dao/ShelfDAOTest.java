package test.dao;

import cccp.database.DatabaseConnection;
import cccp.model.dao.ShelfDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ShelfDAOTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private ShelfDAO shelfDAO;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
        shelfDAO = new ShelfDAO();
        Field databaseConnectionField = ShelfDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(shelfDAO, databaseConnection);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);
        doNothing().when(connection).close();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }


    @Test
    void testGetshelfQuantityByProduct_SQLException() throws SQLException {
        when(connection.prepareStatement("SELECT quantity FROM shelf_inventory WHERE product_id = ?")).thenThrow(new SQLException("Database error"));

        int quantity = shelfDAO.getshelfQuantityByProduct("P001");

        assertEquals(0, quantity);
        verify(connection).prepareStatement("SELECT quantity FROM shelf_inventory WHERE product_id = ?");
        verify(preparedStatement, never()).executeQuery();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testAddToShelf_UpdateSuccess() throws SQLException {
        when(connection.prepareStatement("UPDATE shelf_inventory SET quantity = quantity + ? WHERE product_id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        shelfDAO.addToShelf("P001", 10);

        verify(preparedStatement).setInt(1, 10);
        verify(preparedStatement).setString(2, "P001");
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(connection, never()).prepareStatement("INSERT INTO shelf_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)");
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testAddToShelf_UpdateSQLException() throws SQLException {
        when(connection.prepareStatement("UPDATE shelf_inventory SET quantity = quantity + ? WHERE product_id = ?")).thenThrow(new SQLException("Database error"));

        shelfDAO.addToShelf("P001", 10);

        verify(connection).prepareStatement("UPDATE shelf_inventory SET quantity = quantity + ? WHERE product_id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(connection, never()).prepareStatement("INSERT INTO shelf_inventory (product_id, batch_code, quantity) VALUES (?, ?, ?)");
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testReduceShelfQuantity_Success() throws SQLException {
        when(connection.prepareStatement("UPDATE shelf_inventory SET quantity = quantity - ? WHERE product_id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        shelfDAO.reduceShelfQuantity("P001", 5);

        verify(preparedStatement).setInt(1, 5);
        verify(preparedStatement).setString(2, "P001");
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testReduceShelfQuantity_SQLException() throws SQLException {
        when(connection.prepareStatement("UPDATE shelf_inventory SET quantity = quantity - ? WHERE product_id = ?")).thenThrow(new SQLException("Database error"));

        shelfDAO.reduceShelfQuantity("P001", 5);

        verify(connection).prepareStatement("UPDATE shelf_inventory SET quantity = quantity - ? WHERE product_id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }
}