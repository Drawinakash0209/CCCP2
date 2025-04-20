package cccp.command;

import java.util.UUID;

import cccp.BillReport;
import cccp.ReportService;
import cccp.model.dao.BillDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//import cccp.command.Command;
public class BillReportCommand implements Command {
    private ReportService reportService;
    private final HttpServletRequest request;
	private final HttpServletResponse response;
		private String resultKey;

    public BillReportCommand(HttpServletRequest request, HttpServletResponse response) {
        this.reportService = new ReportService(new BillDAO());
        this.request = request;
        this.response = response;
        this.resultKey = UUID.randomUUID().toString(); // Default result key
    }

    @Override
    public void execute() {
    	try {
    		BillReport billReport = reportService.generateAllBillReports();
			request.setAttribute("bills", billReport.getBills());
			request.getRequestDispatcher("billReport.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
    	}
    }

	@Override
	public String getResultKey() {
		// TODO Auto-generated method stub
		return 	resultKey;
	}
}
