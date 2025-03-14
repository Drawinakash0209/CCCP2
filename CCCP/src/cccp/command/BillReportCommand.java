package cccp.command;

import cccp.BillReport;
import cccp.ReportService;
import cccp.model.dao.BillDAO;

public class BillReportCommand implements Command {
    private ReportService reportService;

    public BillReportCommand() {
        this.reportService = new ReportService(new BillDAO());
    }

    @Override
    public void execute() {
        BillReport billReport = reportService.generateAllBillReports();
        billReport.display();
    }
}
