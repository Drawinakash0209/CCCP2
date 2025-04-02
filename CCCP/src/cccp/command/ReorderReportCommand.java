package cccp.command;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReorderReportCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	

	
	public ReorderReportCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;

	}

	@Override
	public void execute() {
		//reportService.generateReorderReport().displayReport();
		try {
			request.getRequestDispatcher("ReorderReportServlet").forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
