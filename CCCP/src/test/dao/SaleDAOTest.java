package test.dao;

import cccp.database.DatabaseConnection;
import cccp.model.Sale;
import cccp.model.dao.SaleDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SaleDAOTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private SaleDAO saleDAO;
    private Sale sale;
    private Date testDate;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
        saleDAO = new SaleDAO();
        Field databaseConnectionField = SaleDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(saleDAO, databaseConnection);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);
        doNothing().when(connection).close();

        testDate = new Date();
        sale = new Sale.SaleBuilder()
                .setSaleId(1)
                .setProductCode("P001")
                .setQuantitySold(10)
                .setTotalRevenue(199.90)
                .setSalesDate(testDate)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddSale_Success() throws SQLException {
        when(connection.prepareStatement("INSERT INTO sales (product_code, quantity_sold, total_revenue, sales_date, sale_type) VALUES (?,?,?,?,?)")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        saleDAO.addSale(sale);

        verify(preparedStatement).setString(1, "P001");
        verify(preparedStatement).setInt(2, 10);
        verify(preparedStatement).setDouble(3, 199.90);
        verify(preparedStatement).setDate(4, new java.sql.Date(testDate.getTime()));
        verify(preparedStatement).setString(5, null);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testAddSale_SQLException() throws SQLException {
        when(connection.prepareStatement("INSERT INTO sales (product_code, quantity_sold, total_revenue, sales_date, sale_type) VALUES (?,?,?,?,?)")).thenThrow(new SQLException("Database error"));

        saleDAO.addSale(sale);

        verify(connection).prepareStatement("INSERT INTO sales (product_code, quantity_sold, total_revenue, sales_date, sale_type) VALUES (?,?,?,?,?)");
        verify(preparedStatement, never()).executeUpdate();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

   

    @Test
    void testGetSalesByDate_SQLException() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM sales WHERE sales_date = ?")).thenThrow(new SQLException("Database error"));

        List<Sale> sales = saleDAO.getSalesByDate(testDate);

        assertTrue(sales.isEmpty());
        verify(connection).prepareStatement("SELECT * FROM sales WHERE sales_date = ?");
        verify(preparedStatement, never()).executeQuery();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }
}