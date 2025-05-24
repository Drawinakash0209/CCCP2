package test.dao;

import cccp.database.DatabaseConnection;
import cccp.model.Category;
import cccp.model.dao.CategoryDAO;
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

public class CategoryDAOTest {

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

    private CategoryDAO categoryDAO;
    private Category category;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
        categoryDAO = new CategoryDAO();
        Field databaseConnectionField = CategoryDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(categoryDAO, databaseConnection);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);
        doNothing().when(connection).close();

        category = new Category(0, "TestCategory");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddItem_Success() throws SQLException {
        when(connection.prepareStatement("INSERT INTO categories (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        int result = categoryDAO.addItem(category);

        assertEquals(1, result);
        assertEquals(1, category.getId());
        verify(preparedStatement).setString(1, "TestCategory");
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
    void testAddItem_FailedInsertion_ReturnsZero() throws SQLException {
        when(connection.prepareStatement("INSERT INTO categories (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        int result = categoryDAO.addItem(category);

        assertEquals(0, result);
        assertEquals(0, category.getId());
        verify(preparedStatement).setString(1, "TestCategory");
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement, never()).getGeneratedKeys();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testAddItem_SQLException_ReturnsZero() throws SQLException {
        when(connection.prepareStatement("INSERT INTO categories (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)).thenThrow(new SQLException("Database error"));

        int result = categoryDAO.addItem(category);

        assertEquals(0, result);
        assertEquals(0, category.getId());
        verify(connection).prepareStatement("INSERT INTO categories (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }





    @Test
    void testViewAllItems_EmptyResultSet() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery("SELECT * FROM categories")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        categoryDAO.viewAllItems();

        verify(statement).executeQuery("SELECT * FROM categories");
        verify(resultSet).next();
        verify(statement).close();
        verify(resultSet).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }






    @Test
    void testUpdateItem_Success() throws SQLException {
        when(connection.prepareStatement("UPDATE categories SET name = ? WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        categoryDAO.updateItem(new Category(1, "UpdatedCategory"));

        verify(preparedStatement).setString(1, "UpdatedCategory");
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testUpdateItem_NoCategoryFound() throws SQLException {
        when(connection.prepareStatement("UPDATE categories SET name = ? WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        categoryDAO.updateItem(new Category(1, "UpdatedCategory"));

        verify(preparedStatement).setString(1, "UpdatedCategory");
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testUpdateItem_SQLException() throws SQLException {
        when(connection.prepareStatement("UPDATE categories SET name = ? WHERE id = ?")).thenThrow(new SQLException("Database error"));

        categoryDAO.updateItem(new Category(1, "UpdatedCategory"));

        verify(connection).prepareStatement("UPDATE categories SET name = ? WHERE id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveItem_Success() throws SQLException {
        when(connection.prepareStatement("DELETE FROM categories WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        categoryDAO.removeItem(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveItem_NoCategoryFound() throws SQLException {
        when(connection.prepareStatement("DELETE FROM categories WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        categoryDAO.removeItem(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testRemoveItem_SQLException() throws SQLException {
        when(connection.prepareStatement("DELETE FROM categories WHERE id = ?")).thenThrow(new SQLException("Database error"));

        categoryDAO.removeItem(1);

        verify(connection).prepareStatement("DELETE FROM categories WHERE id = ?");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }
}