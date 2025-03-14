package cccp;
import cccp.model.User;
import cccp.strategy.AuthenticationStrategy;

public class EmployeeAuthentication implements AuthenticationStrategy {

	@Override
	public boolean authentication(User user, String password) {
		return user.getUserType().equalsIgnoreCase("Employee") && user.getPassword().equals(password); 
	}

}
