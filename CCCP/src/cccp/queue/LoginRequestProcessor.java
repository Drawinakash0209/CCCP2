package cccp.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final BlockingQueue<LoginRequest> queue = new LinkedBlockingQueue<>(100);
    private static final ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            executorService.submit(new LoginWorker());
        }
    }

    public static void addLoginRequest(LoginRequest req) {
        if(!queue.offer(req)) {
        	req.getResultFuture().completeExceptionally(new Exception("Queue is full"));
        }
    }

    public static void shutdown() {
        executorService.shutdown();
    }

    private static class LoginWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    LoginRequest req = queue.take();
                    handleLogin(req);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error processing login: " + e.getMessage());
                }
            }
        }

        private void handleLogin(LoginRequest loginRequest) {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            LoginResult result = new LoginResult();

            User user = new UserDAO().getUserByUsername(username);
            if (user != null) {
                AuthenticationStrategy strategy = AuthenticationFactory.getAuthenticationStrategy(user.getUserType());
                if (strategy.authentication(user, password)) {
                    result.status = "success";
                    result.userType = user.getUserType();
                    result.user = user;
                    System.out.println("[Thread: " + Thread.currentThread().getName() + "] Logged in: " + username);
                } else {
                    result.status = "failed";
                    System.out.println("[Thread: " + Thread.currentThread().getName() + "] Incorrect password: " + username);
                }
            } else {
                result.status = "failed";
                System.out.println("[Thread: " + Thread.currentThread().getName() + "] User not found: " + username);
            }

            loginRequest.getResultFuture().complete(result);
        }
    }

    public static class LoginResult {
        public String status = "pending";
        public String userType = null;
        public User user = null;
    }
}

