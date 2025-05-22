package cccp.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import cccp.queue.CommandProcessor.CommandResult;

public interface Command {
    void execute() throws Exception;
    String getResultKey();
    CompletableFuture<CommandResult> getResultFuture();
}