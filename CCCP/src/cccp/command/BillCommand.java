package cccp.command;

import cccp.controller.BillController;

public class BillCommand implements Command {
	
	private BillController billController;
	
	public BillCommand(BillController billController) {
		this.billController = billController;
	}

	@Override
	public void execute() {
		billController.run();
	}

}
