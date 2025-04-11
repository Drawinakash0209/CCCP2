package cccp.queue;

import jakarta.servlet.http.HttpServletRequest;

public class LoginRequest {
	public HttpServletRequest request;
	public HttpServletRequest response;
	
	public LoginRequest(HttpServletRequest request, HttpServletRequest response) {
		this.request = request;
		this.response = response;
	}

}
