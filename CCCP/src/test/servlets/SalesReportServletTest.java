package test.servlets;

import cccp.SaleReport;
import cccp.servlets.SalesReportServlet;
import cccp.ReportService;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;
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
import java.util.Date;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class SalesReportServletTest {

    private SalesReportServlet servlet;
    private Method doPostMethod;

    @Mock
    private ReportService reportService;

    @Mock
    private SaleDAOInterface saleDAO;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private SaleReport saleReport;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new SalesReportServlet();

        // Inject mocked ReportService using reflection
        setPrivateField(servlet, "reportService", reportService);

        // Set up reflection for private doPost method
        doPostMethod = SalesReportServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
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
    void testDoPost_ValidDate_GeneratesSalesReport() throws Exception {
        // Arrange
        String inputDate = "2025-05-24";
        when(request.getParameter("date")).thenReturn(inputDate);
        when(reportService.generateSalesReport(any(Date.class))).thenReturn(saleReport);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateSalesReport(any(Date.class));
        verify(request).setAttribute("saleReport", saleReport);
        verify(request).setAttribute("inputDate", inputDate);
        verify(request).getRequestDispatcher("salesReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoPost_InvalidDate_SetsError() throws Exception {
        // Arrange
        String inputDate = "invalid-date";
        when(request.getParameter("date")).thenReturn(inputDate);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verifyNoInteractions(reportService);
        verify(request).setAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
        verify(request).getRequestDispatcher("salesReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoPost_EmptyDate_NoReportGenerated() throws Exception {
        // Arrange
        when(request.getParameter("date")).thenReturn("");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verifyNoInteractions(reportService);
        verify(request).getRequestDispatcher("salesReport.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    }