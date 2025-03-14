package cccp;

import cccp.model.dao.UserDAO;
import cccp.model.dao.UserDAOInterface;
import cccp.strategy.CustomerRegistrationStrategy;
import cccp.strategy.CustomerRegistrationStrategyInterface;

public class UserRegistrationFactory {
	private static UserDAOInterface userDAO = new UserDAO();
	
	 public static CustomerRegistrationStrategyInterface getCustomerRegistrationStrategy() {
	        return new CustomerRegistrationStrategy(userDAO);
	   }


}
