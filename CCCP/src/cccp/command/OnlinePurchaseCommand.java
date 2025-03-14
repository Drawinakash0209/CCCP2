package cccp.command;

import cccp.OnlinePurchaseController;

public class OnlinePurchaseCommand implements Command {

    private final OnlinePurchaseController onlinePurchaseController;

    public OnlinePurchaseCommand(OnlinePurchaseController onlinePurchaseController) {
        this.onlinePurchaseController = onlinePurchaseController;
    }

    @Override
    public void execute() {
        onlinePurchaseController.run();
    }
}