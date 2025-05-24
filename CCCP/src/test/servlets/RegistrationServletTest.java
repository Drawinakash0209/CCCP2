package test.servlets;

import cccp.UserRegistrationFactory;
import cccp.servlets.RegistrationServlet;
import cccp.strategy.CustomerRegistrationStrategy;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class RegistrationServletTest {

    private RegistrationServlet servlet;
    private Method doGetMethod;
    private Method doPostMethod;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CustomerRegistrationStrategy registrationStrategy;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new RegistrationServlet();

        // Set up reflection for private methods
        doGetMethod = RegistrationServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = RegistrationServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        // Initialize mocks
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void testDoGet_ShowsRegistrationForm() throws Exception {
        // Act
        doGetMethod.invoke(servlet, request, response);

        // Assert
        verify(request).getRequestDispatcher("register.jsp");
        verify(requestDispatcher).forward(request, response);
        verifyNoInteractions(response);
    }

    @Test
    void testDoPost_SuccessfulRegistration() throws Exception {
        // Arrange
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password123");

        try (var mockedStatic = mockStatic(UserRegistrationFactory.class)) {
            mockedStatic.when(UserRegistrationFactory::getCustomerRegistrationStrategy).thenReturn(registrationStrategy);
            doNothing().when(registrationStrategy).register("testUser", "password123");

            // Act
            doPostMethod.invoke(servlet, request, response);

            // Assert
            verify(request).setAttribute("registrationSuccess", true);
            verify(request).getRequestDispatcher("login.jsp");
            verify(requestDispatcher).forward(request, response);
            verifyNoInteractions(response);
        }
    }

    @Test
    void testDoPost_RegistrationFailure_InvalidInput() throws Exception {
        // Arrange
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password123");

        try (var mockedStatic = mockStatic(UserRegistrationFactory.class)) {
            mockedStatic.when(UserRegistrationFactory::getCustomerRegistrationStrategy).thenReturn(registrationStrategy);
            doThrow(new IllegalArgumentException("Username already exists")).when(registrationStrategy).register("testUser", "password123");

            // Act
            doPostMethod.invoke(servlet, request, response);

            // Assert
            verify(request).setAttribute("registrationError", "Registration failed: Username already exists");
            verify(request).getRequestDispatcher("register.jsp");
            verify(requestDispatcher).forward(request, response);
            verifyNoInteractions(response);
        }
    }

    @Test
    void testDoPost_RegistrationFailure_DatabaseError() throws Exception {
        // Arrange
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password123");

        try (var mockedStatic = mockStatic(UserRegistrationFactory.class)) {
            mockedStatic.when(UserRegistrationFactory::getCustomerRegistrationStrategy).thenReturn(registrationStrategy);
            doThrow(new RuntimeException("Database error")).when(registrationStrategy).register("testUser", "password123");

            // Act
            doPostMethod.invoke(servlet, request, response);

            // Assert
            verify(request).setAttribute("registrationError", "Registration failed: Database error");
            verify(request).getRequestDispatcher("register.jsp");
            verify(requestDispatcher).forward(request, response);
            verifyNoInteractions(response);
        }
    }
}