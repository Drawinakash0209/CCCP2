package test.servlets;

import cccp.model.Product;
import cccp.model.User;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.ProductService;
import cccp.servlets.ProductServlet;
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
import java.util.List;

import static org.mockito.Mockito.*;

public class ProductServletTest {

    private ProductServlet servlet;

    @Mock
    private ProductService productService;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private BatchDAOInterface batchDAO;

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
    private Method doGetMethod;
    private Method doPostMethod;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Initialize servlet and inject mocked ProductService
        servlet = new ProductServlet();
        setPrivateField(servlet, "productService", productService);

        // Set up reflection for private methods
        doGetMethod = ProductServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = ProductServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        // Initialize mocks
        loggedInUser = new User(1, "testUser", "password123", "admin");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    // Helper method to set private fields via reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // Helper method to create a Product with the Builder pattern
    private Product createProduct(String id, String name, double price, int categoryId, int reorderLevel) {
        return new Product.Builder()
                .setId(id)
                .setName(name)
                .setPrice(price)
                .setCategoryId(categoryId)
                .setReorderLevel(reorderLevel)
                .build();
    }

    // doGet Tests
    @Test
    void testDoGet_NoUser_RedirectsToLogin() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(productService, requestDispatcher, printWriter);
    }

    @Test
    void testDoGet_FetchProducts_Success() throws Exception {
        // Arrange
        List<Product> products = List.of(createProduct("P001", "Laptop", 1000.0, 1, 5));
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(productService).getAllProducts();
        verify(request).setAttribute("products", products);
        verify(request).getRequestDispatcher("product.jsp");
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
        verify(printWriter).write("{\"success\": false, \"message\": \"Please login first\"}");
        verifyNoInteractions(productService, requestDispatcher);
    }

    @Test
    void testDoPost_CreateProduct_Success() throws Exception {
        // Arrange
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("id")).thenReturn("P001");
        when(request.getParameter("name")).thenReturn("Laptop");
        when(request.getParameter("price")).thenReturn("1000.0");
        when(request.getParameter("category_id")).thenReturn("1");
        when(request.getParameter("reorder_level")).thenReturn("5");

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(productService).addProduct(argThat(product ->
                product.getId().equals("P001") &&
                        product.getName().equals("Laptop") &&
                        Math.abs(product.getPrice() - 1000.0) < 0.001 &&
                        product.getCategoryId() == 1 &&
                        product.getReorderLevel() == 5
        ));
        verify(response).setContentType("application/json");
        verify(printWriter).write("{\"success\": true, \"message\": \"Product added successfully\"}");
        verifyNoInteractions(requestDispatcher);
    }

    

    @Test
    void testDoPost_InvalidAction_FallsBackToDoGet() throws Exception {
        // Arrange
        when(request.getParameter("action")).thenReturn("invalid");
        List<Product> products = List.of(createProduct("P001", "Laptop", 1000.0, 1, 5));
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(productService).getAllProducts();
        verify(request).setAttribute("products", products);
        verify(request).getRequestDispatcher("product.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(printWriter);
    }

   

    
}