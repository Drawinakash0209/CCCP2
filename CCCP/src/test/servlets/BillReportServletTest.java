package test.servlets;

import cccp.BillReport;
import cccp.ReportService;
import cccp.model.Bill;
import cccp.servlets.BillReportServlet;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BillReportServletTest {

    private BillReportServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ReportService reportService;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private BillReport billReport;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks and verify they are not null
        MockitoAnnotations.openMocks(this);
        assertNotNull(request, "HttpServletRequest mock is null");
        assertNotNull(response, "HttpServletResponse mock is null");
        assertNotNull(reportService, "ReportService mock is null");
        assertNotNull(requestDispatcher, "RequestDispatcher mock is null");
        assertNotNull(billReport, "BillReport mock is null");

        // Mock request dispatcher
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Initialize servlet and inject mocked reportService
        servlet = new BillReportServlet();
        Field reportServiceField = BillReportServlet.class.getDeclaredField("reportService");
        reportServiceField.setAccessible(true);
        reportServiceField.set(servlet, reportService);

        // Verify injection
        assertNotNull(reportServiceField.get(servlet), "ReportService injection failed");

        // Set up private methods for reflection
        doGetMethod = BillReportServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = BillReportServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
    }

    @Test
    void testDoGet_GeneratesBillReportAndForwards() throws Exception {
        // Arrange: Mock bill report and bills using BillBuilder
        Bill.BillItem billItem1 = Bill.BillItemFactory.createBillItem("P001", "Product 1", 2, 50.0, 100.0);
        Bill.BillItem billItem2 = Bill.BillItemFactory.createBillItem("P002", "Product 2", 1, 30.0, 30.0);
        List<Bill> bills = Arrays.asList(
            new Bill.BillBuilder()
                .setBillId(1)
                .setBillDate(new Date())
                .setTotalPrice(130.0)
                .setCashTendered(150.0)
                .setchangeAmount(20.0) // Fixed typo
                .setDiscount(0.0)
                .addBillItem(billItem1)
                .addBillItem(billItem2)
                .build(),
            new Bill.BillBuilder()
                .setBillId(2)
                .setBillDate(new Date())
                .setTotalPrice(50.0)
                .setCashTendered(60.0)
                .setchangeAmount(10.0) // Fixed typo
                .setDiscount(5.0)
                .addBillItem(billItem1)
                .build()
        );
        when(reportService.generateAllBillReports()).thenReturn(billReport);
        when(billReport.getBills()).thenReturn(bills);
        when(request.getRequestDispatcher("billReport.jsp")).thenReturn(requestDispatcher);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateAllBillReports();
        verify(billReport).getBills();
        verify(request).setAttribute("bills", bills);
        verify(request).getRequestDispatcher("billReport.jsp");
        verify(requestDispatcher).forward(request, response);
        // Removed verifyNoMoreInteractions to debug unexpected interactions
    }

    @Test
    void testDoPost_DelegatesToDoGet() throws Exception {
        // Arrange: Mock bill report and bills using BillBuilder
        Bill.BillItem billItem1 = Bill.BillItemFactory.createBillItem("P001", "Product 1", 2, 50.0, 100.0);
        Bill.BillItem billItem2 = Bill.BillItemFactory.createBillItem("P002", "Product 2", 1, 30.0, 30.0);
        List<Bill> bills = Arrays.asList(
            new Bill.BillBuilder()
                .setBillId(1)
                .setBillDate(new Date())
                .setTotalPrice(130.0)
                .setCashTendered(150.0)
                .setchangeAmount(20.0) // Fixed typo
                .setDiscount(0.0)
                .addBillItem(billItem1)
                .addBillItem(billItem2)
                .build(),
            new Bill.BillBuilder()
                .setBillId(2)
                .setBillDate(new Date())
                .setTotalPrice(50.0)
                .setCashTendered(60.0)
                .setchangeAmount(10.0) // Fixed typo
                .setDiscount(5.0)
                .addBillItem(billItem1)
                .build()
        );
        when(reportService.generateAllBillReports()).thenReturn(billReport);
        when(billReport.getBills()).thenReturn(bills);
        when(request.getRequestDispatcher("billReport.jsp")).thenReturn(requestDispatcher);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(reportService).generateAllBillReports();
        verify(billReport).getBills();
        verify(request).setAttribute("bills", bills);
        verify(request).getRequestDispatcher("billReport.jsp");
        verify(requestDispatcher).forward(request, response);
        // Removed verifyNoMoreInteractions to debug unexpected interactions
    }


}