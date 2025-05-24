package test.batch;

import cccp.database.DatabaseConnection;
import cccp.model.Batch;
import cccp.model.dao.BatchDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BatchDAOTest {

    @InjectMocks
    private BatchDAO batchDAO;

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private Batch batch;
    private Date purchaseDate;
    private Date expiryDate;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);

        // Use reflection to inject the mocked DatabaseConnection into BatchDAO
        batchDAO = new BatchDAO();
        Field databaseConnectionField = BatchDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(batchDAO, databaseConnection);

        purchaseDate = new Date();
        expiryDate = new Date();
        batch = new Batch("B001", 100, purchaseDate, expiryDate);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddBatch_Success() throws SQLException {
        // Arrange
        String productId = "P001";
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        batchDAO.addBatch(batch, productId);

        // Assert
        verify(preparedStatement).setString(1, productId);
        verify(preparedStatement).setString(2, batch.getBatchcode());
        verify(preparedStatement).setInt(3, batch.getQuantity());
        verify(preparedStatement).setDate(4, new java.sql.Date(batch.getPurchaseDate().getTime()));
        verify(preparedStatement).setDate(5, new java.sql.Date(batch.getExpiryDate().getTime()));
        verify(preparedStatement).executeUpdate();
        verify(databaseConnection).closeConnection();
    }

    @Test
    void testAddBatch_SQLException_HandlesException() throws SQLException {
        // Arrange
        String productId = "P001";
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Act
        batchDAO.addBatch(batch, productId);

        // Assert
        verify(preparedStatement).executeUpdate();
        verify(databaseConnection).closeConnection();
    }

    @Test
    void testUpdateBatch_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        batchDAO.updateBatch(batch);

        // Assert
        verify(preparedStatement).setInt(1, batch.getQuantity());
        verify(preparedStatement).setDate(2, new java.sql.Date(batch.getPurchaseDate().getTime()));
        verify(preparedStatement).setDate(3, new java.sql.Date(batch.getExpiryDate().getTime()));
        verify(preparedStatement).setString(4, batch.getBatchcode());
        verify(preparedStatement).executeUpdate();
        verify(databaseConnection).closeConnection();
    }

    @Test
    void testUpdateBatch_SQLException_HandlesException() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Act
        batchDAO.updateBatch(batch);

        // Assert
        verify(preparedStatement).executeUpdate();
        verify(databaseConnection).closeConnection();
    }

    @Test
    void testUpdateBatchQuantity_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        batchDAO.updateBatchQuantity(batch);

        // Assert
        verify(preparedStatement).setInt(1, batch.getQuantity());
        verify(preparedStatement).setString(2, batch.getBatchcode());
        verify(preparedStatement).executeUpdate();
        verify(databaseConnection).closeConnection();
    }

    @Test
    void testUpdateBatchQuantity_SQLException_ThrowsRuntimeException() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> batchDAO.updateBatchQuantity(batch));
        verify(preparedStatement).executeUpdate();
        verify(databaseConnection).closeConnection();
    }



    @Test
    void testGetTotalQuantityForProduct_Success() throws SQLException {
        // Arrange
        String productId = "P001";
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("total_quantity")).thenReturn(500);

        // Act
        int result = batchDAO.getTotalQuantityForProduct(productId);

        // Assert
        assertEquals(500, result);
        verify(preparedStatement).setString(1, productId);
        verify(preparedStatement).executeQuery();
        verify(databaseConnection).closeConnection();
    }

    @Test
    void testGetTotalQuantityForProduct_NoResults_ReturnsZero() throws SQLException {
        // Arrange
        String productId = "P001";
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Act
        int result = batchDAO.getTotalQuantityForProduct(productId);

        // Assert
        assertEquals(0, result);
        verify(preparedStatement).setString(1, productId);
        verify(preparedStatement).executeQuery();
        verify(databaseConnection).closeConnection();
    }

    @Test
    void testGetTotalQuantityForProduct_SQLException_ReturnsZero() throws SQLException {
        // Arrange
        String productId = "P001";
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        // Act
        int result = batchDAO.getTotalQuantityForProduct(productId);

        // Assert
        assertEquals(0, result);
        verify(preparedStatement).setString(1, productId);
        verify(preparedStatement).executeQuery();
        verify(databaseConnection).closeConnection();
    }
}