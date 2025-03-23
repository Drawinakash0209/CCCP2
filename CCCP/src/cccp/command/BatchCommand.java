package cccp.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BatchCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	

	
	public BatchCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public void execute() {
		try {
			request.getRequestDispatcher("BatchServlet").forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
 