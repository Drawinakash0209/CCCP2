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
    private static final int THREAD_POOL_SIZE = 5; // Fixed number of threads
    private static final BlockingQueue<LoginRequest> queue = new LinkedBlockingQueue<>();
    private static final ConcurrentHashMap<String, LoginResult> loginResults = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            Thread worker = new Thread(new LoginWorker(), "LoginWorker-" + i);
            worker.setDaemon(true);
            worker.start();
        }
    }

    public static void addLoginRequest(LoginRequest req) {
        queue.offer(req);
    }

    public static LoginResult getLoginResult(String username) {
        return loginResults.get(username);
    }

    public static void clearLoginResult(String username) {
        loginResults.remove(username);
    }

    private static class LoginWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    LoginRequest req = queue.take(); // Waits if queue is empty
                    handleLogin(req);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleLogin(LoginRequest loginRequest) {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            User user = new UserDAO().getUserByUsername(username);
            LoginResult result = new LoginResult();

            if (user != null) {
                AuthenticationStrategy strategy = AuthenticationFactory.getAuthenticationStrategy(user.getUserType());
                if (strategy.authentication(user, password)) {
                    result.status = "success";
                    result.userType = user.getUserType();
                    result.user = user;
                    System.out.println("✅ [Thread: " + Thread.currentThread().getName() + "] Logged in: " + username);
                } else {
                    result.status = "failed";
                    System.out.println("❌ [Thread: " + Thread.currentThread().getName() + "] Incorrect password: " + username);
                }
            } else {
                result.status = "failed";
                System.out.println("❌ [Thread: " + Thread.currentThread().getName() + "] User not found: " + username);
            }

            loginResults.put(username, result);
        }
    }

    // Inner class to hold login result
    public static class LoginResult {
        public String status = "pending";
        public String userType = null;
        public User user = null;
    }
}

