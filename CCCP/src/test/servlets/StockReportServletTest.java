package test.servlets;

import cccp.servlets.StockReportServlet;
import cccp.ReportService;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.StockItem;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.*;

public class StockReportServletTest {

    private StockReportServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private ReportService reportService;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private StockItem stockItem1;

    @Mock
    private StockItem stockItem2;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new StockReportServlet();

        // Inject mocked ReportService using reflection
        setPrivateField(servlet, "reportService", reportService);

        // Set up reflection for private methods
        doGetMethod = StockReportServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = StockReportServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        // Initialize mocks
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    // Helper method to set private fields via reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testDoGet_GeneratesStockReport_Success() throws Exception {
        // Arrange
        List<StockItem> stockItems = List.of(stockItem1, stockItem2);
        when(reportService.generateStockReport()).thenReturn(stockItems);
        when(stockItem1.getProductName()).thenReturn("Laptop");
        when(stockItem2.getProductName()).thenReturn("Mouse");

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateStockReport();
        verify(stockItem1).getProductName();
        verify(stockItem2).getProductName();
        verify(request).setAttribute("stockItems", stockItems);
        verify(request).getRequestDispatcher("StockReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoPost_CallsDoGet() throws Exception {
        // Arrange
        List<StockItem> stockItems = List.of(stockItem1);
        when(reportService.generateStockReport()).thenReturn(stockItems);
        when(stockItem1.getProductName()).thenReturn("Laptop");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateStockReport();
        verify(stockItem1).getProductName();
        verify(request).setAttribute("stockItems", stockItems);
        verify(request).getRequestDispatcher("StockReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoGet_ReportGenerationFailure() throws Exception {
        // Arrange
        when(reportService.generateStockReport()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        try {
            doGetMethod.invoke(servlet, request, response);
        } catch (Exception e) {
            // Assert
            verify(reportService).generateStockReport();
            verifyNoInteractions(stockItem1, stockItem2, request, response, requestDispatcher);
            // Exception is expected to propagate (not caught by servlet)
            assert e.getCause().getMessage().equals("Database error");
        }
    }
}