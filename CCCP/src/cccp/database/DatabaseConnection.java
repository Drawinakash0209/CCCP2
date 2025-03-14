package cccp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	//private static variable to hold single instance, and volatile for thread safety
	private static volatile DatabaseConnection instance;
	
	//private variable to DB connection
	private Connection connection;
	
	
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/syos";
    private static final String USER = "root";
    private static final String PASSWORD = "";
		
	//constructor is private which blocked instantation from outside
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Connection to Database Failed", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
	
	//Method to get the DB connection
	public Connection getConnection() {
		return connection;
	}
	
	
	
	
	 

}
