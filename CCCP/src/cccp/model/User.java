package cccp.model;

public class User {
	private int id;
	private String username;
	private String password;
	private String userType;
	
	public User(int id, String username, String password, String userType) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.userType = userType;
	}
	
	
	 // Getters and Setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }
	

}
