package cccp.queue;

import java.util.concurrent.CompletableFuture;

import cccp.queue.LoginRequestProcessor.LoginResult;

public class LoginRequest {
    private final String username;
    private final String password;
    private final CompletableFuture<LoginResult> resultFuture;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
        this.resultFuture = new CompletableFuture<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public CompletableFuture<LoginResult> getResultFuture() { return resultFuture; }
}
