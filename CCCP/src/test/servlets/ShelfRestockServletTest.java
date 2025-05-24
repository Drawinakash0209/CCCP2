package test.servlets;

import cccp.model.User;
import cccp.servlets.ShelfRestockServlet;
import cccp.service.ProductService;
import cccp.service.ShelfServiceInterface;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.ShelfDAOInterface;
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

public class ShelfRestockServletTest {

    private ShelfRestockServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private ShelfServiceInterface shelfService;

    @Mock
    private ProductService productService;

    @Mock
    private BatchDAOInterface batchDAO;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private ShelfDAOInterface shelfDAO;

    @Mock
    private BatchSelectionStrategy batchSelectionStrategy;

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
        servlet = new ShelfRestockServlet();

        // Inject mocked ShelfService and ProductService using reflection
        setPrivateField(servlet, "shelfService", shelfService);
        setPrivateField(servlet, "productService", productService);

        // Set up reflection for private methods
        doGetMethod = ShelfRestockServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = ShelfRestockServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        // Initialize mocks
        loggedInUser = new User(1, "testUser", "password123", "admin");
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    // Helper method to set private fields via reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testDoGet_NoUser_RedirectsToLogin() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(shelfService, productService, requestDispatcher, printWriter);
    }

    @Test
    void testDoGet_AuthenticatedUser_ShowsRestockPage() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(request).getRequestDispatcher("shelfRestock.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(shelfService, productService, response, printWriter);
    }

    @Test
    void testDoPost_NoUser_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(printWriter).println("{\"success\": false, \"message\": \"Please login first\"}");
        verifyNoInteractions(shelfService, productService, requestDispatcher);
    }

    @Test
    void testDoPost_ValidInput_SuccessfulRestock() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("10");
        doNothing().when(shelfService).restockShelf(eq("P001"), eq(10), any(Date.class), any(BatchSelectionStrategy.class));
        doNothing().when(shelfService).addRestockListener(productService);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(shelfService).restockShelf(eq("P001"), eq(10), any(Date.class), any(BatchSelectionStrategy.class));
        verify(shelfService).addRestockListener(productService);
        verify(response).setContentType("application/json");
        verify(printWriter).println("{\"success\": true, \"message\": \"Shelf restocked successfully\"}");
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void testDoPost_MissingProductId_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("");
        when(request.getParameter("quantity")).thenReturn("10");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Product ID is required\"}");
        verifyNoInteractions(shelfService, productService, requestDispatcher);
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
        verifyNoInteractions(shelfService, productService, requestDispatcher);
    }

    @Test
    void testDoPost_NegativeQuantity_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("-5");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Quantity must be greater than zero\"}");
        verifyNoInteractions(shelfService, productService, requestDispatcher);
    }

    @Test
    void testDoPost_IllegalStateException_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("10");
        doThrow(new IllegalStateException("Insufficient batch stock")).when(shelfService)
                .restockShelf(eq("P001"), eq(10), any(Date.class), any(BatchSelectionStrategy.class));

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(shelfService).restockShelf(eq("P001"), eq(10), any(Date.class), any(BatchSelectionStrategy.class));
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Insufficient batch stock\"}");
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void testDoPost_UnexpectedException_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("10");
        doThrow(new RuntimeException("Database error")).when(shelfService)
                .restockShelf(eq("P001"), eq(10), any(Date.class), any(BatchSelectionStrategy.class));

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(shelfService).restockShelf(eq("P001"), eq(10), any(Date.class), any(BatchSelectionStrategy.class));
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(printWriter).println("{\"success\": false, \"message\": \"Unexpected error: Database error\"}");
        verifyNoInteractions(requestDispatcher);
    }
}