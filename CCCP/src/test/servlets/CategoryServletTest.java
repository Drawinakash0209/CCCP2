package test.servlets;

import cccp.model.Category;
import cccp.model.User;
import cccp.service.CategoryService;
import cccp.servlets.CategoryServlet;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class CategoryServletTest {

    private CategoryServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private CategoryService categoryService;

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
        servlet = new CategoryServlet();

        // Use reflection to inject the mocked categoryService
        Field categoryServiceField = CategoryServlet.class.getDeclaredField("categoryService");
        categoryServiceField.setAccessible(true);
        categoryServiceField.set(servlet, categoryService);

        // Set up reflection for doGet and doPost
        doGetMethod = CategoryServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = CategoryServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
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
        List<Category> categories = Arrays.asList(
                new Category(1, "Electronics"),
                new Category(2, "Clothing")
        );
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("categories", categories);
        verify(request).getRequestDispatcher("category.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(printWriter);
    }

    @Test
    void testDoGet_NoUser_RedirectsToLogin() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(categoryService, requestDispatcher, printWriter);
    }

    @Test
    void testDoGet_CategoryServiceThrowsException() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(categoryService.getAllCategories()).thenThrow(new RuntimeException("Database error"));

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(request).setAttribute("message", "Error: Database error");
        verify(request).getRequestDispatcher("category.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(printWriter);
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
        verifyNoInteractions(categoryService, requestDispatcher);
    }

    @Test
    void testDoPost_MissingAction_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn(null);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(printWriter).println("{\"success\": false, \"message\": \"Action parameter is missing\"}");
        verifyNoInteractions(categoryService, requestDispatcher);
    }

    @Test
    void testDoPost_CreateCategory_Success() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("categoryName")).thenReturn("Electronics");
        when(categoryService.createCategory("Electronics")).thenReturn(1);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(categoryService).createCategory("Electronics");
        verify(printWriter).println("{\"success\": true, \"message\": \"Category added successfully! New ID: 1\"}");
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void testDoPost_UpdateCategory_Success() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("categoryName")).thenReturn("Electronics");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(categoryService).updateCategory(1, "Electronics");
        verify(printWriter).println("{\"success\": true, \"message\": \"Category updated successfully\"}");
        verifyNoInteractions(requestDispatcher);
    }



    @Test
    void testDoPost_UpdateCategory_InvalidId_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("invalid");
        when(request.getParameter("categoryName")).thenReturn("Electronics");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println(startsWith("{\"success\": false, \"message\": \"Error: "));
        verifyNoInteractions(categoryService, requestDispatcher);
    }

    @Test
    void testDoPost_UpdateCategory_MissingIdAndName_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("id")).thenReturn("");
        when(request.getParameter("categoryName")).thenReturn("");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Error: Category ID and name are required.\"}");
        verifyNoInteractions(categoryService, requestDispatcher);
    }

    @Test
    void testDoPost_DeleteCategory_Success() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(categoryService).deleteCategory(1);
        verify(printWriter).println("{\"success\": true, \"message\": \"Category deleted successfully\"}");
        verifyNoInteractions(requestDispatcher);
    }

    @Test
    void testDoPost_DeleteCategory_MissingId_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println("{\"success\": false, \"message\": \"Error: Category ID is required for deletion.\"}");
        verifyNoInteractions(categoryService, requestDispatcher);
    }

    @Test
    void testDoPost_DeleteCategory_InvalidId_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("invalid");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).println(startsWith("{\"success\": false, \"message\": \"Error: "));
        verifyNoInteractions(categoryService, requestDispatcher);
    }

    @Test
    void testDoPost_InvalidAction_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("invalid");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(printWriter).println("{\"success\": false, \"message\": \"Invalid action: invalid\"}");
        verifyNoInteractions(categoryService, requestDispatcher);
    }

    @Test
    void testDoPost_CategoryServiceThrowsException_ReturnsJsonError() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("categoryName")).thenReturn("Electronics");
        when(categoryService.createCategory("Electronics")).thenThrow(new RuntimeException("Database error"));

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(categoryService).createCategory("Electronics");
        verify(printWriter).println("{\"success\": false, \"message\": \"Error: Database error\"}");
        verifyNoInteractions(requestDispatcher);
    }
}