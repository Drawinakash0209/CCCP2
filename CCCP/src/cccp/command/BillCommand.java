package cccp.command;

import java.util.UUID;

import cccp.controller.BillController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BillCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private String resultKey;

	
	public BillCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.resultKey = UUID.randomUUID().toString();
	}

	@Override
	public void execute() {
		try {
			request.getRequestDispatcher("BillServlet").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getResultKey() {
		// TODO Auto-generated method stub
		return resultKey;
	}

}
