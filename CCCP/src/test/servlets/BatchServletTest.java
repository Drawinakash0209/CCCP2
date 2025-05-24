package test.servlets;

import cccp.model.Batch;
import cccp.model.User;
import cccp.service.BatchService;
import cccp.servlets.BatchServlet;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class BatchServletTest {

    private BatchServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private BatchService batchService;

    @Mock
    private RequestDispatcher requestDispatcher;

    private User user;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a test user with the required constructor parameters
        user = new User(1, "testUser", "password123", "admin");

        // Mock session and request behavior
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // Initialize servlet and inject mocked batchService
        servlet = new BatchServlet();
        Field batchServiceField = BatchServlet.class.getDeclaredField("batchService");
        batchServiceField.setAccessible(true);
        batchServiceField.set(servlet, batchService);

        // Set up private methods for reflection
        doGetMethod = BatchServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = BatchServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
    }

    @Test
    void testDoGet_UserNotLoggedIn() throws Exception {
        // Arrange: No user in session
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void testDoGet_UserLoggedIn() throws Exception {
        // Arrange: User is logged in
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getRequestDispatcher("batch.jsp")).thenReturn(requestDispatcher);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(requestDispatcher).forward(request, response);
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoPost_UserNotLoggedIn() throws Exception {
        // Arrange: No user in session
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(requestDispatcher, batchService);
    }

    @Test
    void testDoPost_CreateAction_ValidInput() throws Exception {
        // Arrange: User is logged in, valid create action
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("product_id")).thenReturn("P001");
        when(request.getParameter("batch_id")).thenReturn("B001");
        when(request.getParameter("quantity")).thenReturn("100");
        when(request.getParameter("purchase_date")).thenReturn("2025-05-24");
        when(request.getParameter("expiry_date")).thenReturn("2026-05-24");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(batchService).addBatch("P001", "B001", 100, "2025-05-24", "2026-05-24");
        verify(response).sendRedirect("BatchServlet");
        verifyNoMoreInteractions(batchService, response);
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void testDoPost_CreateAction_InvalidQuantityFormat() throws Exception {
        // Arrange: User is logged in, invalid quantity
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("product_id")).thenReturn("P001");
        when(request.getParameter("batch_id")).thenReturn("B001");
        when(request.getParameter("quantity")).thenReturn("invalid");
        when(request.getParameter("purchase_date")).thenReturn("2025-05-24");
        when(request.getParameter("expiry_date")).thenReturn("2026-05-24");
        when(request.getRequestDispatcher("batch.jsp")).thenReturn(requestDispatcher);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("errorMessage", "Invalid quantity format.");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(batchService);
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoPost_CreateAction_InvalidDateFormat() throws Exception {
        // Arrange: User is logged in, invalid date format
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("product_id")).thenReturn("P001");
        when(request.getParameter("batch_id")).thenReturn("B001");
        when(request.getParameter("quantity")).thenReturn("100");
        when(request.getParameter("purchase_date")).thenReturn("invalid-date");
        when(request.getParameter("expiry_date")).thenReturn("2026-05-24");
        when(request.getRequestDispatcher("batch.jsp")).thenReturn(requestDispatcher);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD.");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(batchService);
        verifyNoMoreInteractions(response);
    }



    @Test
    void testDoPost_InvalidAction() throws Exception {
        // Arrange: User is logged in, invalid action
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("action")).thenReturn("invalid");
        when(request.getRequestDispatcher("batch.jsp")).thenReturn(requestDispatcher);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(batchService);
        verifyNoMoreInteractions(response);
    }
}