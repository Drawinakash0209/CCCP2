package cccp.command;

import cccp.ReportService;

public class ReorderReportCommand implements Command {
	
	private ReportService reportService;
	
	public ReorderReportCommand(ReportService reportService) {
		this.reportService = reportService;
	}

	@Override
	public void execute() {
		reportService.generateReorderReport().displayReport();
	}

}
