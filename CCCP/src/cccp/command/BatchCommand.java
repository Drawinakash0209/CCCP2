package cccp.command;

import cccp.controller.BatchController;

public class BatchCommand implements Command {
	private BatchController batchController;
	
	public BatchCommand(BatchController batchController) {
		this.batchController = batchController;
	}

	@Override
	public void execute() {
		batchController.run();
	}

}
 