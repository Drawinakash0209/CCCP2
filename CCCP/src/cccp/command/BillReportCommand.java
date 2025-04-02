package cccp.command;

import cccp.BillReport;
import cccp.ReportService;
import cccp.model.dao.BillDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BillReportCommand implements Command {
    private ReportService reportService;
    private final HttpServletRequest request;
	private final HttpServletResponse response;

    public BillReportCommand(HttpServletRequest request, HttpServletResponse response) {
        this.reportService = new ReportService(new BillDAO());
        this.request = request;
        this.response = response;
    }

    @Override
    public void execute() {
//        BillReport billReport = reportService.generateAllBillReports();
//        billReport.display();
    	
    	try {
    		BillReport billReport = reportService.generateAllBillReports();
			request.setAttribute("bills", billReport.getBills());
			request.getRequestDispatcher("billReport.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
    	}
    }
}
