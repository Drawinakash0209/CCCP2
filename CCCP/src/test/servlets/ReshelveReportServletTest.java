package test.servlets;

import cccp.ReshelveReport;
import cccp.model.Product;
import cccp.servlets.ReshelveReportServlet;
import cccp.ReportService;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.ShelfDAOInterface;
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

public class ReshelveReportServletTest {

    private ReshelveReportServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private ReportService reportService;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private ShelfDAOInterface shelfDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new ReshelveReportServlet();

        // Inject mocked ReportService using reflection
        setPrivateField(servlet, "reportService", reportService);

        // Set up reflection for private methods
        doGetMethod = ReshelveReportServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = ReshelveReportServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
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
    void testDoGet_GeneratesReshelveReport_Success() throws Exception {
        // Arrange
        List<ReshelveReport.Item> items = List.of(
                new ReshelveReport.Item("P001", "Laptop", 30),
                new ReshelveReport.Item("P002", "Mouse", 40)
        );
        ReshelveReport report = new ReshelveReport(items);
        when(reportService.generateReshelveReport()).thenReturn(report);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateReshelveReport();
        verify(request).setAttribute("report", report);
        verify(request).getRequestDispatcher("/reshelveReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoPost_CallsDoGet() throws Exception {
        // Arrange
        List<ReshelveReport.Item> items = List.of(
                new ReshelveReport.Item("P001", "Laptop", 30)
        );
        ReshelveReport report = new ReshelveReport(items);
        when(reportService.generateReshelveReport()).thenReturn(report);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateReshelveReport();
        verify(request).setAttribute("report", report);
        verify(request).getRequestDispatcher("/reshelveReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoGet_ReportGenerationFailure() throws Exception {
        // Arrange
        when(reportService.generateReshelveReport()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        try {
            doGetMethod.invoke(servlet, request, response);
        } catch (Exception e) {
            // Assert
            verify(reportService).generateReshelveReport();
            verifyNoInteractions(request, response, requestDispatcher);
            // Exception is expected to propagate (not caught by servlet)
            assert e.getCause().getMessage().equals("Database error");
        }
    }
}