package cccp.command;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import cccp.queue.CommandProcessor.CommandResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReshelveReportCommand implements Command {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final String resultKey;
    private final CompletableFuture<CommandResult> resultFuture;

    public ReshelveReportCommand(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.resultKey = UUID.randomUUID().toString();
        this.resultFuture = new CompletableFuture<>();
    }

    @Override
    public void execute() throws Exception {
        try {
            request.getRequestDispatcher("ReshelveReportServlet").forward(request, response);
            resultFuture.complete(new CommandResult("success", "Reshelve report command executed"));
        } catch (Exception e) {
            resultFuture.complete(new CommandResult("failed", "Error executing reshelve report command: " + e.getMessage()));
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