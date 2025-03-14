package cccp.command;

import cccp.controller.OnlineOrderController;

public class OnlineOrderCommand implements Command{
	private final OnlineOrderController onlineOrderController;
	
	 public OnlineOrderCommand(OnlineOrderController onlineOrderController) {
	        this.onlineOrderController = onlineOrderController;
	 }
	 
	 @Override
	 public void execute() {
	        onlineOrderController.manageOnlineOrders();
	 }

}
