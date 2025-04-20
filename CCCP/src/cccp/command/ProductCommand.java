package cccp.command;

import java.util.UUID;

import cccp.controller.ProductController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProductCommand implements Command {
	
	private final HttpServletRequest request;
	private final HttpServletResponse response;	
	private String resultKey;
	
	// Constructor
	public ProductCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.resultKey = UUID.randomUUID().toString();
	}

	// Execute method
	@Override
	public void execute() {
		try {
			request.getRequestDispatcher("ProductServlet").forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getResultKey() {
		// TODO Auto-generated method stub
		return resultKey;
	}

}
