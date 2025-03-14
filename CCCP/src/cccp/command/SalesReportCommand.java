package cccp.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import cccp.ReportService;
import cccp.SaleReport;
import cccp.model.dao.BillDAO;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAO;

public class SalesReportCommand implements Command {
	private ReportService reportService;
	
	public SalesReportCommand() {
		this.reportService = new ReportService(new SaleDAO(), new ProductDAO());
	}

	@Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the date for the sales report (yyyy-MM-dd): ");
        String inputDate = scanner.nextLine();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(inputDate);
            SaleReport saleReport = reportService.generateSalesReport(date);
            saleReport.display();
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

}


