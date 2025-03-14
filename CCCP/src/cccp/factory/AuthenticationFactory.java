package cccp.factory;

import cccp.CustomerAuthentication;
import cccp.EmployeeAuthentication;
import cccp.strategy.AuthenticationStrategy;

public class AuthenticationFactory {
	
	public static AuthenticationStrategy getAuthenticationStrategy(String userType) {
		switch(userType){
			case "Employee":
				return new EmployeeAuthentication();
			case "Customer":
				return new CustomerAuthentication();
			default:
				throw new IllegalArgumentException("Invalid user");
		}
	}

}
