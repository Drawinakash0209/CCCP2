package test.servlets;

import cccp.PercentageDiscount;
import cccp.model.Bill;
import cccp.service.BillingServiceInterface;
import cccp.service.ShelfServiceInterface;
import cccp.servlets.BillServlet;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BillServletTest {

    private BillServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ShelfServiceInterface shelfService;

    @Mock
    private BillingServiceInterface billingService;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock request dispatcher
        when(request.getRequestDispatcher("Bill.jsp")).thenReturn(requestDispatcher);

        // Initialize servlet and inject mocked services
        servlet = new BillServlet();
        Field shelfServiceField = BillServlet.class.getDeclaredField("shelfService");
        shelfServiceField.setAccessible(true);
        shelfServiceField.set(servlet, shelfService);

        Field billingServiceField = BillServlet.class.getDeclaredField("billingService");
        billingServiceField.setAccessible(true);
        billingServiceField.set(servlet, billingService);

        // Set up private methods for reflection
        doGetMethod = BillServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = BillServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
    }

    @Test
    void testDoGet_ForwardsToBillJsp() throws Exception {
        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(request).getRequestDispatcher("Bill.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(billingService, shelfService);
    }

    @Test
    void testDoPost_ValidBillCreation() throws Exception {
        // Arrange
        String[] productIds = {"P001", "P002"};
        String[] quantities = {"2", "1"};
        when(request.getParameterValues("productId")).thenReturn(productIds);
        when(request.getParameterValues("quantity")).thenReturn(quantities);
        when(request.getParameter("discountRate")).thenReturn("10.0");
        when(request.getParameter("cashTendered")).thenReturn("200.0");

        Bill.BillItem billItem1 = Bill.BillItemFactory.createBillItem("P001", "Product 1", 2, 50.0, 100.0);
        Bill.BillItem billItem2 = Bill.BillItemFactory.createBillItem("P002", "Product 2", 1, 30.0, 30.0);
        when(billingService.createBillItem("P001", 2)).thenReturn(billItem1);
        when(billingService.createBillItem("P002", 1)).thenReturn(billItem2);

        Bill bill = new Bill.BillBuilder()
                .setBillId(1)
                .setBillDate(new Date())
                .setTotalPrice(130.0)
                .setCashTendered(200.0)
                .setchangeAmount(70.0)
                .setDiscount(10.0)
                .addBillItem(billItem1)
                .addBillItem(billItem2)
                .build();
        when(billingService.createBill(anyList(), eq(200.0))).thenReturn(bill);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(billingService).createBillItem("P001", 2);
        verify(billingService).createBillItem("P002", 1);
        verify(billingService).setDiscount(any(PercentageDiscount.class));
        verify(billingService).createBill(eq(Arrays.asList(billItem1, billItem2)), eq(200.0));
        verify(request).setAttribute("bill", bill);
        verify(request).setAttribute("success", "Bill created successfully");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(shelfService);
    }

    @Test
    void testDoPost_NullProductIds() throws Exception {
        // Arrange
        when(request.getParameterValues("productId")).thenReturn(null);
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("error", "No valid products selected.");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(billingService, shelfService);
    }

    @Test
    void testDoPost_MismatchedProductIdsAndQuantities() throws Exception {
        // Arrange
        String[] productIds = {"P001", "P002"};
        String[] quantities = {"1"};
        when(request.getParameterValues("productId")).thenReturn(productIds);
        when(request.getParameterValues("quantity")).thenReturn(quantities);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("error", "No valid products selected.");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(billingService, shelfService);
    }

    @Test
    void testDoPost_EmptyProductId() throws Exception {
        // Arrange
        String[] productIds = {"", "P002"};
        String[] quantities = {"2", "1"};
        when(request.getParameterValues("productId")).thenReturn(productIds);
        when(request.getParameterValues("quantity")).thenReturn(quantities);
        Bill.BillItem billItem = Bill.BillItemFactory.createBillItem("P002", "Product 2", 1, 30.0, 30.0);
        when(billingService.createBillItem("P002", 1)).thenReturn(billItem);
        when(request.getParameter("discountRate")).thenReturn("5.0");
        when(request.getParameter("cashTendered")).thenReturn("100.0");

        Bill bill = new Bill.BillBuilder()
                .setBillId(1)
                .setBillDate(new Date())
                .setTotalPrice(30.0)
                .setCashTendered(100.0)
                .setchangeAmount(70.0)
                .setDiscount(5.0)
                .addBillItem(billItem)
                .build();
        when(billingService.createBill(anyList(), eq(100.0))).thenReturn(bill);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(billingService).createBillItem("P002", 1);
        verify(billingService).setDiscount(any(PercentageDiscount.class));
        verify(billingService).createBill(eq(Collections.singletonList(billItem)), eq(100.0));
        verify(request).setAttribute("bill", bill);
        verify(request).setAttribute("success", "Bill created successfully");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(shelfService);
    }

    @Test
    void testDoPost_InvalidQuantityFormat() throws Exception {
        // Arrange
        String[] productIds = {"P001"};
        String[] quantities = {"invalid"};
        when(request.getParameterValues("productId")).thenReturn(productIds);
        when(request.getParameterValues("quantity")).thenReturn(quantities);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("error", "Invalid quantity format for product: P001");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(billingService, shelfService);
    }

    @Test
    void testDoPost_NonPositiveQuantity() throws Exception {
        // Arrange
        String[] productIds = {"P001"};
        String[] quantities = {"0"};
        when(request.getParameterValues("productId")).thenReturn(productIds);
        when(request.getParameterValues("quantity")).thenReturn(quantities);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("error", "Quantity must be greater than zero for product: P001");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(billingService, shelfService);
    }

}