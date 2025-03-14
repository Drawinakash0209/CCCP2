package cccp.command;

import cccp.ReportService;


public class StockReportCommand implements Command {
	private ReportService reportService;
	
	 public StockReportCommand(ReportService reportService) {
	        this.reportService = reportService;
	 }
	 
	@Override
	public void execute() {
        reportService.generateStockReport();
    }

}
