package cccp.command;

import cccp.controller.OnlineOrderController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OnlineOrderCommand implements Command{
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	 public OnlineOrderCommand(HttpServletRequest request, HttpServletResponse response) {
	        this.request = request;
	        this.response = response;
	 }
	 
	 @Override
	 public void execute() {
	        try {
	            request.getRequestDispatcher("OnlineOrderServlet").forward(request, response);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }

}
