package test.servlets;

import cccp.ReorderReport;
import cccp.model.Product;
import cccp.servlets.ReorderReportServlet;
import cccp.ReportService;
import cccp.model.dao.ProductDAOInterface;
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

public class ReorderReportServletTest {

    private ReorderReportServlet servlet;
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

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new ReorderReportServlet();

        // Inject mocked ReportService using reflection
        setPrivateField(servlet, "reportService", reportService);

        // Set up reflection for private methods
        doGetMethod = ReorderReportServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = ReorderReportServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
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

    // Helper method to create a Product with the Builder pattern
    private Product createProduct(String id, String name, int quantity, int reorderLevel) {
        return new Product.Builder()
                .setId(id)
                .setName(name)
                .setPrice(1000.0) // Arbitrary price, not used in report
                .setCategoryId(1) // Arbitrary category, not used in report
                .setQuantity(quantity)
                .setReorderLevel(reorderLevel)
                .build();
    }

    @Test
    void testDoGet_GeneratesReorderReport_Success() throws Exception {
        // Arrange
        List<Product> products = List.of(
                createProduct("P001", "Laptop", 20, 50),
                createProduct("P002", "Mouse", 10, 50)
        );
        ReorderReport report = new ReorderReport(products);
        when(reportService.generateReorderReport()).thenReturn(report);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateReorderReport();
        verify(request).setAttribute("products", products);
        verify(request).getRequestDispatcher("/reorderReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoPost_CallsDoGet() throws Exception {
        // Arrange
        List<Product> products = List.of(
                createProduct("P001", "Laptop", 20, 50)
        );
        ReorderReport report = new ReorderReport(products);
        when(reportService.generateReorderReport()).thenReturn(report);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateReorderReport();
        verify(request).setAttribute("products", products);
        verify(request).getRequestDispatcher("/reorderReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoGet_ReportGenerationFailure() throws Exception {
        // Arrange
        when(reportService.generateReorderReport()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        try {
            doGetMethod.invoke(servlet, request, response);
        } catch (Exception e) {
            // Assert
            verify(reportService).generateReorderReport();
            verifyNoInteractions(request, response, requestDispatcher);
            // Exception is expected to propagate (not caught by servlet)
            assert e.getCause().getMessage().equals("Database error");
        }
    }
}