package test.servlets;

import cccp.model.Batch;
import cccp.model.Bill;
import cccp.model.Product;
import cccp.model.User;
import cccp.model.dao.*;
import cccp.queue.CustomerCheckoutProcessor;
import cccp.queue.OnlineCheckoutRequest;
import cccp.service.BillingService;
import cccp.service.BillingServiceInterface;
import cccp.service.OnlineOrderService;
import cccp.servlets.OnlineShopServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OnlineShopServletTest {

    private OnlineShopServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;
    private Method addToCartMethod;
    private Method updateCartMethod;
    private Method initiateCheckoutMethod;
    private Method processDeliveryDetailsMethod;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private BatchDAOInterface batchDAO;

    @Mock
    private BillDAOInterface billDAO;

    @Mock
    private SaleDAOInterface saleDAO;

    @Mock
    private OnlineInventoryDAOInterface onlineDAO;

    @Mock
    private DeliveryDetailsDAOInterface deliveryDetailsDAO;

    @Mock
    private BillingServiceInterface billingService;

    @Mock
    private OnlineOrderService onlineOrderService;

    @Mock
    private CustomerCheckoutProcessor customerCheckoutProcessor;

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

    @Mock
    private Bill bill;

    private User loggedInUser;
    private Map<String, Integer> cart;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new OnlineShopServlet();
        cart = new HashMap<>(); // Fresh cart for each test

        // Inject mocked static dependencies using reflection
        setStaticField(OnlineShopServlet.class, "productDAO", productDAO);
        setStaticField(OnlineShopServlet.class, "batchDAO", batchDAO);
        setStaticField(OnlineShopServlet.class, "billDAO", billDAO);
        setStaticField(OnlineShopServlet.class, "saleDAO", saleDAO);
        setStaticField(OnlineShopServlet.class, "onlineDAO", onlineDAO);
        setStaticField(OnlineShopServlet.class, "deliveryDetailsDAO", deliveryDetailsDAO);
        setStaticField(OnlineShopServlet.class, "onlineOrderService", onlineOrderService);
        setStaticField(OnlineShopServlet.class, "billingService", billingService);
        setStaticField(OnlineShopServlet.class, "customerCheckoutProcessor", customerCheckoutProcessor);

        // Set up reflection for methods
        doGetMethod = OnlineShopServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = OnlineShopServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        addToCartMethod = OnlineShopServlet.class.getDeclaredMethod("addToCart", HttpServletRequest.class, HttpServletResponse.class);
        addToCartMethod.setAccessible(true);

        updateCartMethod = OnlineShopServlet.class.getDeclaredMethod("updateCart", HttpServletRequest.class, HttpServletResponse.class);
        updateCartMethod.setAccessible(true);

        initiateCheckoutMethod = OnlineShopServlet.class.getDeclaredMethod("initiateCheckout", HttpServletRequest.class, HttpServletResponse.class);
        initiateCheckoutMethod.setAccessible(true);

        processDeliveryDetailsMethod = OnlineShopServlet.class.getDeclaredMethod("processDeliveryDetails", HttpServletRequest.class, HttpServletResponse.class);
        processDeliveryDetailsMethod.setAccessible(true);

        // Initialize mocks
        loggedInUser = new User(1, "testUser", "password123", "admin");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(loggedInUser);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getContextPath()).thenReturn("/app");
        when(bill.getBillId()).thenReturn(123);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Reset static fields to prevent state leakage
        setStaticField(OnlineShopServlet.class, "productDAO", new ProductDAO());
        setStaticField(OnlineShopServlet.class, "batchDAO", new BatchDAO());
        setStaticField(OnlineShopServlet.class, "billDAO", new BillDAO());
        setStaticField(OnlineShopServlet.class, "saleDAO", new SaleDAO());
        setStaticField(OnlineShopServlet.class, "onlineDAO", new OnlineInventoryDAO());
        setStaticField(OnlineShopServlet.class, "deliveryDetailsDAO", new DeliveryDetailsDAO());
        setStaticField(OnlineShopServlet.class, "onlineOrderService", new OnlineOrderService(batchDAO, onlineDAO));
        setStaticField(OnlineShopServlet.class, "billingService", new BillingService(billDAO, productDAO, onlineOrderService, saleDAO));
        setStaticField(OnlineShopServlet.class, "customerCheckoutProcessor", new CustomerCheckoutProcessor());
    }

    // Helper method to set static fields via reflection
    private void setStaticField(Class<?> clazz, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        field.set(null, value);
    }

    // Helper method to create a Product with batches
    private Product createProduct(String id, String name, double price, int categoryId, int quantity, int reorderLevel) {
        List<Batch> batches = new ArrayList<>();
        if (quantity > 0) {
            batches.add(new Batch(id + "-B1", quantity, null, null));
        }
        return new Product.Builder()
                .setId(id)
                .setName(name)
                .setPrice(price)
                .setCategoryId(categoryId)
                .setBatches(batches)
                .setReorderLevel(reorderLevel)
                .build();
    }

    @Test
    void testDoGet_NoUser_RedirectsToLogin() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(productDAO, requestDispatcher, printWriter);
    }

    @Test
    void testDoGet_ViewProducts_Success() throws Exception {
        // Arrange
        List<Product> products = List.of(createProduct("P001", "Laptop", 1000.0, 1, 10, 5));
        when(productDAO.getAllProducts()).thenReturn(products);
        when(request.getParameter("action")).thenReturn("viewProducts");

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(productDAO).getAllProducts();
        verify(request).setAttribute("products", products);
        verify(request).getRequestDispatcher("product_catalog.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(printWriter);
    }

    @Test
    void testDoGet_ViewCart_Success() throws Exception {
        // Arrange
        cart.put("P001", 2);
        Product product = createProduct("P001", "Laptop", 1000.0, 1, 10, 5);
        when(productDAO.getProductById("P001")).thenReturn(product);
        when(request.getParameter("action")).thenReturn("viewCart");

        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(productDAO).getProductById("P001");
        verify(request).setAttribute(eq("detailedCart"), any(Map.class));
        verify(request).setAttribute("cartTotal", 2000.0);
        verify(request).getRequestDispatcher("cart_view.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(printWriter);
    }

    @Test
    void testDoPost_NoUser_RedirectsToLogin() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        doPostMethod.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?error=Please login first");
        verifyNoInteractions(productDAO, requestDispatcher, printWriter);
    }

    @Test
    void testAddToCart_Success() throws Exception {
        // Arrange
        Product product = createProduct("P001", "Laptop", 1000.0, 1, 10, 5);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("2");
        when(productDAO.getProductById("P001")).thenReturn(product);

        // Act
        addToCartMethod.invoke(servlet, request, response);

        // Assert
        verify(productDAO).getProductById("P001");
        verify(session).setAttribute("cart", cart);
        verify(session).setAttribute("message", "Laptop added to cart.");
        verify(response).sendRedirect("/app/onlineShop?action=viewProducts");
        assertEquals(2, cart.get("P001"));
        verifyNoInteractions(printWriter);
    }

    @Test
    void testUpdateCart_Success() throws Exception {
        // Arrange
        cart.put("P001", 2);
        when(request.getParameter("productId")).thenReturn("P001");
        when(request.getParameter("quantity")).thenReturn("3");

        // Act
        updateCartMethod.invoke(servlet, request, response);

        // Assert
        verify(session).setAttribute("message", "Cart updated.");
        verify(response).sendRedirect("/app/onlineShop?action=viewCart");
        assertEquals(3, cart.get("P001"));
        verifyNoInteractions(printWriter);
    }

    @Test
    void testInitiateCheckout_Success() throws Exception {
        // Arrange
        cart.put("P001", 2);

        // Act
        initiateCheckoutMethod.invoke(servlet, request, response);

        // Assert
        verify(request).getRequestDispatcher("checkout_details.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(printWriter);
    }

    @Test
    void testProcessDeliveryDetails_Success() throws Exception {
        // Arrange
        cart.put("P001", 2);
        Product product = createProduct("P001", "Laptop", 1000.0, 1, 10, 5);
        when(productDAO.getProductById("P001")).thenReturn(product);
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("address")).thenReturn("123 Main St");
        when(billingService.generateOnlineBill(anyInt(), anyMap())).thenReturn(bill);
        when(deliveryDetailsDAO.saveDeliveryDetails(argThat(details ->
                "BILL123".equals(details.getBillId()) &&
                        loggedInUser.getId() == details.getCustomerId() &&
                        "John Doe".equals(details.getName()) &&
                        "1234567890".equals(details.getPhoneNumber()) &&
                        "123 Main St".equals(details.getDeliveryAddress())
        ))).thenReturn(1);

        // Mock CustomerCheckoutProcessor
        doAnswer(invocation -> {
            OnlineCheckoutRequest checkoutRequest = invocation.getArgument(0);
            checkoutRequest.getResultFuture().complete(
                    new CustomerCheckoutProcessor.CheckoutResult("success", bill)
            );
            return null;
        }).when(customerCheckoutProcessor).addCheckoutRequest(any(OnlineCheckoutRequest.class));

        // Act
        processDeliveryDetailsMethod.invoke(servlet, request, response);

        // Assert
        verify(productDAO).getProductById("P001");
        verify(billingService).generateOnlineBill(eq(loggedInUser.getId()), anyMap());
        verify(deliveryDetailsDAO).saveDeliveryDetails(any());
        verify(customerCheckoutProcessor).addCheckoutRequest(any(OnlineCheckoutRequest.class));
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(session).removeAttribute("cart");
        verify(session).setAttribute("bill", bill);
        verify(session).setAttribute("message", "Order placed successfully!");
        verify(printWriter).write("{\"status\":\"success\",\"message\":\"Order placed successfully!\"}");
    }

    @Test
    void testProcessDeliveryDetails_CheckoutFailure() throws Exception {
        // Arrange
        cart.put("P001", 2);
        Product product = createProduct("P001", "Laptop", 1000.0, 1, 10, 5);
        when(productDAO.getProductById("P001")).thenReturn(product);
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("address")).thenReturn("123 Main St");
        when(billingService.generateOnlineBill(anyInt(), anyMap())).thenThrow(new RuntimeException("Insufficient stock"));

        // Mock CustomerCheckoutProcessor
        doAnswer(invocation -> {
            OnlineCheckoutRequest checkoutRequest = invocation.getArgument(0);
            checkoutRequest.getResultFuture().complete(
                    new CustomerCheckoutProcessor.CheckoutResult("failed", "Checkout failed: Insufficient stock")
            );
            return null;
        }).when(customerCheckoutProcessor).addCheckoutRequest(any(OnlineCheckoutRequest.class));

        // Act
        processDeliveryDetailsMethod.invoke(servlet, request, response);

        // Assert
        verify(productDAO).getProductById("P001");
        verify(billingService).generateOnlineBill(eq(loggedInUser.getId()), anyMap());
        verify(customerCheckoutProcessor).addCheckoutRequest(any(OnlineCheckoutRequest.class));
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(printWriter).write("{\"status\":\"error\",\"message\":\"Checkout failed: Insufficient stock\"}");
        verifyNoInteractions(deliveryDetailsDAO);
    }

    @Test
    void testProcessDeliveryDetails_CheckoutException() throws Exception {
        // Arrange
        cart.put("P001", 2);
        Product product = createProduct("P001", "Laptop", 1000.0, 1, 10, 5);
        when(productDAO.getProductById("P001")).thenReturn(product);
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("address")).thenReturn("123 Main St");
        when(billingService.generateOnlineBill(anyInt(), anyMap())).thenThrow(new RuntimeException("Checkout timeout"));

        // Mock CustomerCheckoutProcessor
        doAnswer(invocation -> {
            OnlineCheckoutRequest checkoutRequest = invocation.getArgument(0);
            checkoutRequest.getResultFuture().complete(
                    new CustomerCheckoutProcessor.CheckoutResult("failed", "Checkout failed: Checkout timeout")
            );
            return null;
        }).when(customerCheckoutProcessor).addCheckoutRequest(any(OnlineCheckoutRequest.class));

        // Act
        processDeliveryDetailsMethod.invoke(servlet, request, response);

        // Assert
        verify(productDAO).getProductById("P001");
        verify(billingService).generateOnlineBill(eq(loggedInUser.getId()), anyMap());
        verify(customerCheckoutProcessor).addCheckoutRequest(any(OnlineCheckoutRequest.class));
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(printWriter).write("{\"status\":\"error\",\"message\":\"Checkout failed: Checkout timeout\"}");
        verifyNoInteractions(deliveryDetailsDAO);
    }
}