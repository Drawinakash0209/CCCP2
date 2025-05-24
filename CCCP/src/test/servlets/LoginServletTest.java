package test.servlets;

import cccp.model.User;
import cccp.queue.LoginRequest;
import cccp.queue.LoginRequestProcessor;
import cccp.queue.LoginRequestProcessor.LoginResult;
import cccp.servlets.LoginServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private Method doPostMethod;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        // Set up private method once
        doPostMethod = LoginServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
    }

    @Test
    void testSuccessfulLogin_Customer() throws Exception {
        when(request.getParameter("username")).thenReturn("john");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        User mockUser = new User(1, "john", "password", "Customer");
        LoginResult mockResult = new LoginResult();
        mockResult.status = "success";
        mockResult.userType = "Customer";
        mockResult.user = mockUser;

        try (MockedStatic<LoginRequestProcessor> mocked = mockStatic(LoginRequestProcessor.class)) {
            mocked.when(() -> LoginRequestProcessor.addLoginRequest(any(LoginRequest.class)))
                  .thenAnswer(invocation -> {
                      LoginRequest req = invocation.getArgument(0);
                      req.getResultFuture().complete(mockResult);
                      return null;
                  });

            doPostMethod.invoke(servlet, request, response);

            verify(session).setAttribute(eq("user"), eq(mockUser));
            verify(response).sendRedirect("customer.jsp");
        }
    }

    @Test
    void testSuccessfulLogin_Employee() throws Exception {
        when(request.getParameter("username")).thenReturn("jane");
        when(request.getParameter("password")).thenReturn("admin");
        when(request.getSession()).thenReturn(session);

        User mockUser = new User(2, "jane", "admin", "Employee");
        LoginResult mockResult = new LoginResult();
        mockResult.status = "success";
        mockResult.userType = "Employee";
        mockResult.user = mockUser;

        try (MockedStatic<LoginRequestProcessor> mocked = mockStatic(LoginRequestProcessor.class)) {
            mocked.when(() -> LoginRequestProcessor.addLoginRequest(any(LoginRequest.class)))
                  .thenAnswer(invocation -> {
                      LoginRequest req = invocation.getArgument(0);
                      req.getResultFuture().complete(mockResult);
                      return null;
                  });

            doPostMethod.invoke(servlet, request, response);

            verify(session).setAttribute(eq("user"), eq(mockUser));
            verify(response).sendRedirect("employee_dashboard.jsp");
        }
    }

    @Test
    void testFailedLogin_InvalidCredentials() throws Exception {
        when(request.getParameter("username")).thenReturn("fake");
        when(request.getParameter("password")).thenReturn("wrong");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        LoginResult mockResult = new LoginResult();
        mockResult.status = "failed";
        mockResult.userType = null;
        mockResult.user = null;

        try (MockedStatic<LoginRequestProcessor> mocked = mockStatic(LoginRequestProcessor.class)) {
            mocked.when(() -> LoginRequestProcessor.addLoginRequest(any(LoginRequest.class)))
                  .thenAnswer(invocation -> {
                      LoginRequest req = invocation.getArgument(0);
                      req.getResultFuture().complete(mockResult);
                      return null;
                  });

            doPostMethod.invoke(servlet, request, response);

            verify(request).setAttribute(eq("loginError"), anyString());
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void testLoginTimeout_Exception() throws Exception {
        when(request.getParameter("username")).thenReturn("error");
        when(request.getParameter("password")).thenReturn("error");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        try (MockedStatic<LoginRequestProcessor> mocked = mockStatic(LoginRequestProcessor.class)) {
            mocked.when(() -> LoginRequestProcessor.addLoginRequest(any(LoginRequest.class)))
                  .thenAnswer(invocation -> {
                      // Do not complete the future — simulate timeout
                      return null;
                  });

            doPostMethod.invoke(servlet, request, response);

            verify(request).setAttribute(eq("loginError"), contains("Login failed"));
            verify(dispatcher).forward(request, response);
        }
    }
}
