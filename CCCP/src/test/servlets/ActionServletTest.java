package test.servlets;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import cccp.factory.ControllerFactory;
import cccp.servlets.ActionServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

class ActionServletTest {

    private ActionServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ControllerFactory controllerFactory;
    private Method doGetMethod;
    private Method doPostMethod;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        controllerFactory = mock(ControllerFactory.class);

        servlet = new ActionServlet();
        ServletConfig config = mock(ServletConfig.class);
        servlet.init(config);

        // Set controllerFactory using reflection
        Field controllerFactoryField = ActionServlet.class.getDeclaredField("controllerFactory");
        controllerFactoryField.setAccessible(true);
        controllerFactoryField.set(servlet, controllerFactory);

        // Set up private methods for reflection
        doGetMethod = ActionServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        doPostMethod = ActionServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);
    }

    // Update tests to include verifyNoMoreInteractions where appropriate
    @Test
    void testDoGet_ValidOption() throws Exception {
        when(request.getParameter("option")).thenReturn("5");
        doGetMethod.invoke(servlet, request, response);
        verify(controllerFactory).processCommand(eq(5), eq(request), eq(response));
        verifyNoMoreInteractions(controllerFactory, response);
    }

    @Test
    void testDoGet_InvalidOptionNumber() throws Exception {
        when(request.getParameter("option")).thenReturn("99");
        doGetMethod.invoke(servlet, request, response);
        verify(response).sendRedirect("error.jsp");
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoGet_InvalidFormat() throws Exception {
        when(request.getParameter("option")).thenReturn("invalid");
        doGetMethod.invoke(servlet, request, response);
        verify(response).sendRedirect("error.jsp");
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoGet_ExceptionInProcessCommand() throws Exception {
        when(request.getParameter("option")).thenReturn("2");
        doThrow(new RuntimeException("Test exception"))
                .when(controllerFactory).processCommand(eq(2), eq(request), eq(response));
        doGetMethod.invoke(servlet, request, response);
        verify(response).sendRedirect("error.jsp");
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoGet_NoOptionProvided() throws Exception {
        when(request.getParameter("option")).thenReturn(null);
        doGetMethod.invoke(servlet, request, response);
        verify(response).sendRedirect("dashboard.jsp");
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoPost_ValidOption() throws Exception {
        when(request.getParameter("option")).thenReturn("3");
        doPostMethod.invoke(servlet, request, response);
        verify(controllerFactory).processCommand(eq(3), eq(request), eq(response));
        verifyNoMoreInteractions(controllerFactory, response);
    }

    @Test
    void testDoPost_InvalidOptionFormat() throws Exception {
        when(request.getParameter("option")).thenReturn("invalid");
        doPostMethod.invoke(servlet, request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid option format");
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoPost_OutOfRangeOption() throws Exception {
        when(request.getParameter("option")).thenReturn("20");
        doPostMethod.invoke(servlet, request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid command option");
        verifyNoMoreInteractions(response);
    }

    @Test
    void testDoPost_ExceptionInProcessCommand() throws Exception {
        when(request.getParameter("option")).thenReturn("4");
        doThrow(new RuntimeException("Failed"))
                .when(controllerFactory).processCommand(eq(4), eq(request), eq(response));
        doPostMethod.invoke(servlet, request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Command processing failed: Failed");
        verifyNoMoreInteractions(response);
    }
}