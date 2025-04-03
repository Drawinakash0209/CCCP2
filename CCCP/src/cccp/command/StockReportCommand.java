package cccp.command;

import cccp.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class StockReportCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	 public StockReportCommand(HttpServletRequest request, HttpServletResponse response) {
	       this.request = request;
	       this.response = response;	
	 }
	 
	@Override
	public void execute() {
        try {
			request.getRequestDispatcher("StockReportServlet").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
