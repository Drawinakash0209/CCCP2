package test.dao;

import cccp.database.DatabaseConnection;
import cccp.model.Product;
import cccp.model.dao.ProductDAO;
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

public class ProductDAOTest {

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

    private ProductDAO productDAO;
    private Product product;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
        productDAO = new ProductDAO();
        Field databaseConnectionField = ProductDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(productDAO, databaseConnection);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);
        doNothing().when(connection).close();

        product = new Product.Builder()
                .setId("P001")
                .setName("Test Product")
                .setPrice(19.99)
                .setCategoryId(1)
                .setReorderLevel(50)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddItem_Success() throws SQLException {
        when(connection.prepareStatement("INSERT INTO products (id, name, price, category_id, reorder_level) VALUES (?, ?, ?, ?, ?)")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        productDAO.addItem(product);

        verify(preparedStatement).setString(1, "P001");
        verify(preparedStatement).setString(2, "Test Product");
        verify(preparedStatement).setDouble(3, 19.99);
        verify(preparedStatement).setInt(4, 1);
        verify(preparedStatement).setInt(5, 50);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testAddItem_SQLException() throws SQLException {
        when(connection.prepareStatement("INSERT INTO products (id, name, price, category_id, reorder_level) VALUES (?, ?, ?, ?, ?)")).thenThrow(new SQLException("Database error"));

        productDAO.addItem(product);

        verify(connection).prepareStatement("INSERT INTO products (id, name, price, category_id, reorder_level) VALUES (?, ?, ?, ?, ?)");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testGetProductById_Success() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM products WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("id")).thenReturn("P001");
        when(resultSet.getString("name")).thenReturn("Test Product");
        when(resultSet.getDouble("price")).thenReturn(19.99);
        when(resultSet.getInt("category_id")).thenReturn(1);

        Product result = productDAO.getProductById("P001");

        assertNotNull(result);
        assertEquals("P001", result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(19.99, result.getPrice(), 0.01);
        assertEquals(1, result.getCategoryId());
        verify(preparedStatement).setString(1, "P001");
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(resultSet).getString("id");
        verify(resultSet).getString("name");
        verify(resultSet).getDouble("price");
        verify(resultSet).getInt("category_id");
        verify(preparedStatement).close();
        verify(resultSet).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testGetProductById_SQLException() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM products WHERE id = ?")).thenThrow(new SQLException("Database error"));

        Product result = productDAO.getProductById("P001");

        assertNull(result);
        verify(connection).prepareStatement("SELECT * FROM products WHERE id = ?");
        verify(preparedStatement, never()).executeQuery();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testUpdateItem_Success() throws SQLException {
        when(connection.prepareStatement("UPDATE products SET name = ?, price = ?, category_id = ?, reorder_level = ? WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        productDAO.updateItem(product);

        verify(preparedStatement).setString(1, "Test Product");
        verify(preparedStatement).setDouble(2, 19.99);
        verify(preparedStatement).setInt(3, 1);
        verify(preparedStatement).setInt(4, 50);
        verify(preparedStatement).setString(5, "P001");
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testUpdateItem_SQLException() throws SQLException {
        when(connection.prepareStatement("UPDATE products SET name = ?, price = ?, category_id = ?, reorder_level = ? WHERE id = ?")).thenThrow(new SQLException("Database error"));

        productDAO.updateItem(product);

        verify(connection).prepareStatement("UPDATE products SET name = ?, price = ?, category_id = ?, reorder_level = ? WHERE id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveItem_Success() throws SQLException {
        when(connection.prepareStatement("DELETE FROM products WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        productDAO.removeItem("P001");

        verify(preparedStatement).setString(1, "P001");
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveItem_SQLException() throws SQLException {
        when(connection.prepareStatement("DELETE FROM products WHERE id = ?")).thenThrow(new SQLException("Database error"));

        productDAO.removeItem("P001");

        verify(connection).prepareStatement("DELETE FROM products WHERE id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }




}