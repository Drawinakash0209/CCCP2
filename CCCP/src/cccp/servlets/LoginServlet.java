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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginRequestProcessor.addLoginRequest(loginRequest);

        // Poll for login result
        int maxAttempts = 10; // Limit polling attempts
        int attempt = 0;
        LoginResult result = null;

        while (attempt < maxAttempts) {
            result = LoginRequestProcessor.getLoginResult(username);
            if (result != null && !"pending".equals(result.status)) {
                break;
            }
            try {
                Thread.sleep(500); // Wait 500ms before next poll
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new ServletException("Interrupted while polling login result", e);
            }
            attempt++;
        }

        // Clean up login result
        LoginRequestProcessor.clearLoginResult(username);

        if (result == null || "pending".equals(result.status)) {
            request.setAttribute("loginError", "Login processing timed out. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if ("success".equals(result.status)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", result.user);

            // Redirect based on user type
            if ("Employee".equals(result.userType)) {
                response.sendRedirect("employee_dashboard.jsp");
            } else if ("Customer".equals(result.userType)) {
                response.sendRedirect("customer.jsp");
            } else {
                request.setAttribute("loginError", "Invalid user type.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("loginError", "Invalid username or password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}