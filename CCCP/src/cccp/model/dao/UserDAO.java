package cccp.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import cccp.database.DatabaseConnection;
import cccp.model.User;

public class UserDAO implements UserDAOInterface {
	
	@Override
	public User getUserByUsername(String username) {
		try {
			Connection connection  = DatabaseConnection.getInstance().getConnection();
			String query = "SELECT * FROM users WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				return new User(
						resultSet.getInt("id"),
						resultSet.getString("username"),
						resultSet.getString("password"),
						resultSet.getString("user_type")
					);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean saveUser(User user) {
	    try {
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        String query = "INSERT INTO users (username, password, user_type) VALUES (?, ?, ?)";
	        PreparedStatement statement = connection.prepareStatement(query);
	        
	        statement.setString(1, user.getUsername());
	        statement.setString(2, user.getPassword());
	        statement.setString(3, user.getUserType());

	        int rowsAffected = statement.executeUpdate();

	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
