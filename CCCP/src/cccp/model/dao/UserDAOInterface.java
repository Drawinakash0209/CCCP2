package cccp.model.dao;

import cccp.model.User;

public interface UserDAOInterface {
	User getUserByUsername(String username);
    boolean saveUser(User user);
}
