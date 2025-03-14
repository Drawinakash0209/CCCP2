package cccp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import cccp.UserRegistrationFactory;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            UserRegistrationFactory.getCustomerRegistrationStrategy().register(username, password);
            request.setAttribute("registrationSuccess", true);
            request.getRequestDispatcher("login.jsp").forward(request, response); // Redirect to login on success
        } catch (Exception e) {
            request.setAttribute("registrationError", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response); // Back to registration with error
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response); // Show registration form
    }
}