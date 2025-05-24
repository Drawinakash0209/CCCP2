package test.dao;

import cccp.database.DatabaseConnection;
import cccp.model.Bill;
import cccp.model.dao.BillDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BillDAOTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement billStmt;

    @Mock
    private PreparedStatement itemStmt;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSet itemResultSet;

    @InjectMocks
    private BillDAO billDAO;

    private Bill bill;
    private List<Bill.BillItem> billItems;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        closeable = MockitoAnnotations.openMocks(this);
        billDAO = new BillDAO();
        Field databaseConnectionField = BillDAO.class.getDeclaredField("databaseConnection");
        databaseConnectionField.setAccessible(true);
        databaseConnectionField.set(billDAO, databaseConnection);

        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(false);
        doNothing().when(connection).close();

        billItems = new ArrayList<>();
        billItems.add(Bill.BillItemFactory.createBillItem("P001", "Product1", 2, 10.0, 20.0));
        bill = new Bill.BillBuilder()
                .setBillId(1)
                .setBillDate(new Date())
                .setTotalPrice(20.0)
                .setCashTendered(30.0)
                .setchangeAmount(10.0)
                .addBillItem(billItems.get(0))
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }





    @Test
    void testGetNextBillId_NoBills_ReturnsOne() throws SQLException {
        when(connection.prepareStatement("SELECT MAX(bill_id) AS max_id FROM Bill")).thenReturn(billStmt);
        when(billStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("max_id")).thenReturn(0);
        when(resultSet.wasNull()).thenReturn(true);

        int nextId = billDAO.getNextBillId();

        assertEquals(1, nextId);
        verify(billStmt).executeQuery();
        verify(resultSet).next();
        verify(resultSet).wasNull();
        verify(billStmt).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testGetNextBillId_SQLException_ReturnsOne() throws SQLException {
        when(connection.prepareStatement("SELECT MAX(bill_id) AS max_id FROM Bill")).thenThrow(new SQLException("Database error"));

        int nextId = billDAO.getNextBillId();

        assertEquals(1, nextId);
        verify(connection).prepareStatement("SELECT MAX(bill_id) AS max_id FROM Bill");
        verify(billStmt, never()).executeQuery();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testGenerateAllBillReports_ReturnsBills() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM bill")).thenReturn(billStmt);
        when(connection.prepareStatement("SELECT bi.*, p.name, p.price FROM billItems bi JOIN products p ON bi.item_code = p.id WHERE bi.bill_id = ?")).thenReturn(itemStmt);
        when(billStmt.executeQuery()).thenReturn(resultSet);
        when(itemStmt.executeQuery()).thenReturn(itemResultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("bill_Id")).thenReturn(1);
        when(resultSet.getDate("bill_date")).thenReturn(new java.sql.Date(new Date().getTime()));
        when(resultSet.getDouble("total_price")).thenReturn(20.0);
        when(resultSet.getDouble("cash_tendered")).thenReturn(30.0);
        when(resultSet.getDouble("change_amount")).thenReturn(10.0);
        when(itemResultSet.next()).thenReturn(true, false);
        when(itemResultSet.getString("item_code")).thenReturn("P001");
        when(itemResultSet.getString("name")).thenReturn("Product1");
        when(itemResultSet.getInt("quantity")).thenReturn(2);
        when(itemResultSet.getDouble("price")).thenReturn(10.0);
        when(itemResultSet.getDouble("total_price")).thenReturn(20.0);

        List<Bill> bills = billDAO.generateAllBillReports();

        assertEquals(1, bills.size());
        Bill resultBill = bills.get(0);
        assertEquals(1, resultBill.getBillId());
        assertEquals(20.0, resultBill.getTotalPrice());
        assertEquals(1, resultBill.getBillItems().size());
        assertEquals("P001", resultBill.getBillItems().get(0).getProductCode());
        assertEquals("Product1", resultBill.getBillItems().get(0).getproductName()); // Fixed method name
        verify(billStmt).executeQuery();
        verify(itemStmt).executeQuery();
        verify(billStmt).close();
        verify(itemStmt).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }

    @Test
    void testGenerateAllBillReports_EmptyList() throws SQLException {
        when(connection.prepareStatement("SELECT * FROM bill")).thenReturn(billStmt);
        when(billStmt.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Bill> bills = billDAO.generateAllBillReports();

        assertTrue(bills.isEmpty());
        verify(billStmt).executeQuery();
        verify(billStmt).close();
        verify(databaseConnection).closeConnection();
        verify(connection).close();
    }





}