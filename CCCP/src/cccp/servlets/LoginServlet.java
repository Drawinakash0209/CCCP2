package cccp.servlets;
import cccp.factory.AuthenticationFactory;
import cccp.model.User;
import cccp.model.dao.UserDAO;
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

        User user = new UserDAO().getUserByUsername(username); // Use your UserDAO
        if (user != null) {
            AuthenticationStrategy strategy = AuthenticationFactory.getAuthenticationStrategy(user.getUserType()); // Use your factory
            if (strategy.authentication(user, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", user); // Store user in session

                if ("Employee".equalsIgnoreCase(user.getUserType())) {
                    response.sendRedirect("employee_dashboard.jsp"); // Redirect to employee dashboard
                } else {
                    response.sendRedirect("customer_dashboard.jsp"); // Redirect to customer dashboard
                }
                return;
            }
        }
        // Authentication failed
        request.setAttribute("loginError", "Invalid username or password.");
        request.getRequestDispatcher("login.jsp").forward(request, response); // Back to login form with error
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response); // Show login form
    }
}
