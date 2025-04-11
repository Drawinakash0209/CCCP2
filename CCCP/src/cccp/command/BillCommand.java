package cccp.command;

import cccp.controller.BillController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BillCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	
	public BillCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public void execute() {
		try {
			request.getRequestDispatcher("BillServlet").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
