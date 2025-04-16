package cccp.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import cccp.factory.AuthenticationFactory;
import cccp.model.User;
import cccp.model.dao.UserDAO;
import cccp.strategy.AuthenticationStrategy;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginRequestProcessor {
    private static final BlockingQueue<LoginRequest> queue = new LinkedBlockingQueue<>();
    // Store login results: username -> {status: "success"/"failed", userType: "employee"/"customer"/null}
    private static final ConcurrentHashMap<String, LoginResult> loginResults = new ConcurrentHashMap<>();

    static {
        Thread processorThread = new Thread(() -> {
            while (true) {
                try {
                    LoginRequest req = queue.take();
                    handleLogin(req);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        processorThread.setDaemon(true);
        processorThread.start();
    }

    public static void addLoginRequest(LoginRequest req) {
        queue.offer(req); //insert into the queue
    }

    private static void handleLogin(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = new UserDAO().getUserByUsername(username);
        LoginResult result = new LoginResult();

        if (user != null) {
            AuthenticationStrategy strategy = AuthenticationFactory.getAuthenticationStrategy(user.getUserType());
            if (strategy.authentication(user, password)) { // Fixed method name to match typical naming
                result.status = "success";
                result.userType = user.getUserType();
                result.user = user;
                System.out.println("✅ User logged in successfully: " + username);
            } else {
                result.status = "failed";
                System.out.println("❌ Login failed for user: " + username);
            }
        } else {
            result.status = "failed";
            System.out.println("❌ User not found: " + username);
        }

        loginResults.put(username, result);
    }

    public static LoginResult getLoginResult(String username) {
        return loginResults.get(username);
    }

    public static void clearLoginResult(String username) {
        loginResults.remove(username);
    }

    // Inner class to hold login result
    public static class LoginResult {
        public String status = "pending";
        public String userType = null;
        public User user = null;
    }
}

