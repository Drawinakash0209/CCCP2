package test.salesReportTest;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import cccp.ReportService;
import cccp.SaleReport;
import cccp.command.SalesReportCommand;
import cccp.model.Sale;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.SaleDAO;

class SalesReportCommandTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Mock
    SaleDAO mockSaleDAO;

    @Mock
    private ProductDAO mockProductDAO;

    @InjectMocks
    private ReportService reportService;

    @InjectMocks
    private SalesReportCommand command;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void execute_ValidDate_ShouldDisplayCorrectReport() throws Exception {
        // Arrange
        String input = "2023-10-01\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Date testDate = dateFormat.parse("2023-10-01");
        Sale testSale = new Sale.SaleBuilder()
                .setProductCode("P001")
                .setSalesDate(testDate)
                .setQuantitySold(5)
                .setTotalRevenue(100.0)
                .build();
        when(mockSaleDAO.getSalesByDate(testDate)).thenReturn(Arrays.asList(testSale));
        when(mockProductDAO.getProductById("P001")).thenReturn(
                new cccp.model.Product.Builder()
                        .setId("P001")
                        .setName("Test Product")
                        .setPrice(9.99)
                        .setCategoryId(5)
                        .setReorderLevel(10)
                        .setQuantity(50)
                        .build()
        );

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("DAILY SALES REPORT"));
        assertTrue(output.contains("Test Product"));
        assertTrue(output.contains("5"));  // Quantity
        assertTrue(output.contains("100.00"));  // Revenue
        verify(mockSaleDAO).getSalesByDate(testDate);
        verify(mockProductDAO).getProductById("P001");
    }

    @Test
    void execute_InvalidDate_ShouldDisplayErrorMessage() {
        // Arrange
        String input = "invalid-date\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Invalid date format"));
        assertFalse(output.contains("DAILY SALES REPORT"));
    }

    @Test
    void execute_NoSalesData_ShouldDisplayEmptyReport() throws Exception {
        // Arrange
        String input = "2023-10-01\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Date testDate = dateFormat.parse("2023-10-01");
        when(mockSaleDAO.getSalesByDate(testDate)).thenReturn(Arrays.asList());

        // Act
        command.execute();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("DAILY SALES REPORT"));
        assertFalse(output.contains("Test Product"));  // No products in report
        verify(mockSaleDAO).getSalesByDate(testDate);
    }
}
