package cccp.command;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import cccp.OnlinePurchaseController;
import cccp.queue.CommandProcessor.CommandResult;

public class OnlinePurchaseCommand implements Command {

    private final OnlinePurchaseController onlinePurchaseController;
    private String resultKey;

    public OnlinePurchaseCommand(OnlinePurchaseController onlinePurchaseController) {
        this.onlinePurchaseController = onlinePurchaseController;
        this.resultKey = UUID.randomUUID().toString();
    }

    @Override
    public void execute() {
        onlinePurchaseController.run();
    }

	@Override
	public String getResultKey() {
		// TODO Auto-generated method stub
		return resultKey;
	}

	@Override
	public CompletableFuture<CommandResult> getResultFuture() {
		// TODO Auto-generated method stub
		return null;
	}
}