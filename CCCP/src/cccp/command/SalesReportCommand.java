package cccp.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import cccp.ReportService;
import cccp.SaleReport;
import cccp.model.dao.BillDAO;
import cccp.model.dao.ProductDAO;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SalesReportCommand implements Command {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private String resultKey;
	
	
	public SalesReportCommand(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.resultKey = UUID.randomUUID().toString();
	}

	@Override
    public void execute() {		
		try {
			request.getRequestDispatcher("SalesReportServlet").forward(request, response);	
		}catch(Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public String getResultKey() {
		// TODO Auto-generated method stub
		return resultKey;
	}

}


