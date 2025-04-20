package cccp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	//private static variable to hold single instance, and volatile for thread safety
	private static volatile DatabaseConnection instance;
	
	private final ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

		
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/syos";
    private static final String USER = "root";
    private static final String PASSWORD = "";
		
	//constructor is private which blocked instantation from outside
    private DatabaseConnection() {
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    	}catch (ClassNotFoundException e) {
			throw new RuntimeException("MySQL JDBC Driver not found", e);
		}
    }
 
 //Thread safe method to get the connection
    	public static DatabaseConnection getInstance() {
		if (instance == null) {
			synchronized (DatabaseConnection.class) {
				if (instance == null) {
					instance = new DatabaseConnection();
				}
			}
		}
		return instance;
	}
	

    	//Method to get the connection
    	public Connection getConnection() throws SQLException {
    		Connection connection = threadLocalConnection.get();
    		if (connection == null || connection.isClosed()) {
    			connection = DriverManager.getConnection(URL, USER, PASSWORD);
    			threadLocalConnection.set(connection);
			}
    		return connection;
    	}
    	
    	//Method to close the connection
    	public void closeConnection() {
			Connection connection = threadLocalConnection.get();
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					threadLocalConnection.remove();
				}
			}
		}
	
	
	
	 

}
