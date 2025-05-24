package cccp.servlets;
import cccp.factory.AuthenticationFactory;
import cccp.model.User;
import cccp.model.dao.UserDAO;
import cccp.queue.LoginRequest;
import cccp.queue.LoginRequestProcessor;
import cccp.queue.LoginRequestProcessor.LoginResult;
import cccp.strategy.AuthenticationStrategy;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginRequestProcessor.addLoginRequest(loginRequest);

        try {
            LoginResult result = loginRequest.getResultFuture().get(5, TimeUnit.SECONDS);
            System.out.println("Login result for " + username + ": status=" + result.status + ", userType=" + result.userType);

            if ("success".equals(result.status)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", result.user);
                System.out.println("User authenticated: " + result.user.getUsername() + ", Type: " + result.userType);
                if ("Employee".equals(result.userType)) {
                    System.out.println("Redirecting to employee_dashboard.jsp for user: " + result.user.getUsername());
                    response.sendRedirect("employee_dashboard.jsp");
                    return;
                } else if ("Customer".equals(result.userType)) {
                    System.out.println("Redirecting to customer.jsp for user: " + result.user.getUsername());
                    response.sendRedirect("customer.jsp");
                    return;
                } else {
                    System.out.println("Unknown user type for user: " + result.user.getUsername());
                    request.setAttribute("loginError", "Unknown user type");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                }
            } else {
                System.out.println("Login failed for user: " + username + ", Status: " + result.status);
                request.setAttribute("loginError", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("Login error for " + username + ": " + e.getMessage());
            request.setAttribute("loginError", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}