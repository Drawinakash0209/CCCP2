package cccp.command;

import java.util.UUID;

import cccp.OnlinePurchaseController;

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
}