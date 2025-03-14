package cccp;

import cccp.model.User;
import cccp.strategy.AuthenticationStrategy;

public class CustomerAuthentication implements AuthenticationStrategy {

	@Override
	public boolean authentication(User user, String password) {
		return user.getUserType().equalsIgnoreCase("Customer") && user.getPassword().equals(password);
	}

}
