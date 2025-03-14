package cccp.strategy;

import cccp.model.User;

import cccp.model.dao.UserDAOInterface;

public class CustomerRegistrationStrategy implements CustomerRegistrationStrategyInterface {

	private UserDAOInterface userDAO;
	
	public CustomerRegistrationStrategy(UserDAOInterface userDAO) {
		this.userDAO = userDAO;
	}
	
	@Override
	public void register(String username, String password) {
		User user = new User(0, username, password, "Customer");
		userDAO.saveUser(user);		
	}

}
