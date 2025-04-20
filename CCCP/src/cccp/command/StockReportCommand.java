package cccp.command;

import java.util.UUID;

import cccp.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class StockReportCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private String resultKey;
	
	 public StockReportCommand(HttpServletRequest request, HttpServletResponse response) {
	       this.request = request;
	       this.response = response;	
	       this.resultKey = UUID.randomUUID().toString();
	 }
	 
	@Override
	public void execute() {
        try {
			request.getRequestDispatcher("StockReportServlet").forward(request, response);
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
