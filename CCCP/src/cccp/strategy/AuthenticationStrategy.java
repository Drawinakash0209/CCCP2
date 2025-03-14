package cccp.strategy;


import cccp.model.User;

public interface AuthenticationStrategy {
	boolean authentication(User user, String password);
}

