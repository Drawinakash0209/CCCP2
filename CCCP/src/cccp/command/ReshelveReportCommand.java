package cccp.command;

import cccp.ReportService;
import cccp.ReshelveReport;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.SaleDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReshelveReportCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	public ReshelveReportCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
    }
	
	@Override
	public void execute() {
		
//		ReshelveReport report = reportService.generateReshelveReport();
//
//        // Display the report (you can customize how it's displayed)
//        System.out.println("Reshelve Report:");
//        System.out.printf("%-15s | %-25s | %-10s\n", "Product Code", "Product Name", "Quantity to Reshelve");
//        System.out.println("-----------------------------------------------");
//
//        for (ReshelveReport.Item item : report.getItems()) {
//            System.out.printf("%-15s | %-25s | %-10d\n", item.getProductId(), item.getProductName(), item.getQuantityToReshelve());
//        }
		
		try {
			request.getRequestDispatcher("ReshelveReportServlet").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


