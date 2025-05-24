package test.servlets;

import cccp.model.User;
import cccp.service.OnlineOrderServiceInterface;
import cccp.service.ProductService;
import cccp.servlets.OnlineOrderServlet;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import static org.mockito.Mockito.*;

public class OnlineOrderServletTest {

    private OnlineOrderServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private OnlineOrderServiceInterface orderService;

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private PrintWriter printWriter;

    private User loggedInUser;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new OnlineOrderServlet();

        // Use reflection to inject mocked services
        Field orderServiceField = OnlineOrderServlet.class.getDeclaredField("orderService");
        orderServiceField.setAccessible(true);
        orderServiceField.set(servlet, orderService);

        Field productServiceField = OnlineOrderServlet.class.getDeclaredField("productService");
        productServiceField.setAccessible(true);
        productServiceField.set(servlet, productService);

        // Set up reflection for doGet and doPost
        doGetMethod = OnlineOrderServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = OnlineOrderServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        // Initialize mocks
        loggedInUser = new User(1, "testUser", "password123", "admin");
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    // doGet Tests
    @Test
    void testDoGet_UserLoggedIn_Success() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(request).getRequestDispatcher("onlineStockRestock.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(orderService, productService, printWriter);
    }

    @Test
    void testDoGet_NoUser_RedirectsToLogin() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(orderService, productService, requestDispatcher, printWriter);
    }

    // doPost Tests
    @Test
    void testDoPost_NoUser_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(printWriter).println("{\"success\": false, \"message\": \"Please login first\"}");
        verifyNoInteractions(orderService, productService, requestDispatcher);
    }

    @Test
    void testDoPost_MissingProductId_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("");
        when(request.getParameter("quantity")).thenReturn("5");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Product ID is required\"}");
        verifyNoInteractions(orderService, productService, requestDispatcher);
    }

    @Test
    void testDoPost_InvalidQuantityFormat_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("invalid");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Invalid quantity format\"}");
        verifyNoInteractions(orderService, productService, requestDispatcher);
    }

    @Test
    void testDoPost_NonPositiveQuantity_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("0");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Quantity must be greater than zero\"}");
        verifyNoInteractions(orderService, productService, requestDispatcher);
    }

    @Test
    void testDoPost_ValidInput_Success() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("5");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(orderService).allocateStockForOnlineOrder(eq("P001"), eq(5), any(Date.class), any(BatchSelectionStrategy.class));
        verify(orderService).addRestockListener(productService);
        verify(printWriter).println("{\"success\": true, \"message\": \"Online inventories restocked successfully\"}");
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void testDoPost_OrderServiceThrowsException_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("5");
        doThrow(new RuntimeException("Stock allocation failed"))
                .when(orderService).allocateStockForOnlineOrder(eq("P001"), eq(5), any(Date.class), any(BatchSelectionStrategy.class));

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(orderService).allocateStockForOnlineOrder(eq("P001"), eq(5), any(Date.class), any(BatchSelectionStrategy.class));
        verify(printWriter).println("{\"success\": false, \"message\": \"Error restocking online inventories: Stock allocation failed\"}");
        verifyNoInteractions(requestDispatcher);
    }
}