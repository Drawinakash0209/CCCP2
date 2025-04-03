package cccp.command;

import cccp.controller.ShelfController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShelfCommand implements Command{
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	
	 public ShelfCommand(HttpServletRequest request, HttpServletResponse response) {
	        this.request = request;
	        this.response = response;
	 }

	@Override
	public void execute() {
		try {
			request.getRequestDispatcher("ShelfRestockServlet").forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
