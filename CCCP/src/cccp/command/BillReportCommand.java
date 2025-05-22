package cccp.command;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import cccp.BillReport;
import cccp.ReportService;
import cccp.model.dao.BillDAO;
import cccp.queue.CommandProcessor.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BillReportCommand implements Command {
    private final ReportService reportService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final String resultKey;
    private final CompletableFuture<CommandResult> resultFuture;

    public BillReportCommand(HttpServletRequest request, HttpServletResponse response) {
        this.reportService = new ReportService(new BillDAO());
        this.request = request;
        this.response = response;
        this.resultKey = UUID.randomUUID().toString();
        this.resultFuture = new CompletableFuture<>();
    }

    @Override
    public void execute() throws Exception {
        try {
            BillReport billReport = reportService.generateAllBillReports();
            request.setAttribute("bills", billReport.getBills());
            request.getRequestDispatcher("billReport.jsp").forward(request, response);
            resultFuture.complete(new CommandResult("success", "Bill report command executed"));
        } catch (Exception e) {
            resultFuture.complete(new CommandResult("failed", "Error executing bill report command: " + e.getMessage()));
            throw e;
        }
    }

    @Override
    public String getResultKey() {
        return resultKey;
    }

    @Override
    public CompletableFuture<CommandResult> getResultFuture() {
        return resultFuture;
    }
}