package cccp.command;

import cccp.controller.ShelfController;

public class ShelfCommand implements Command{
	private final ShelfController shelfController;
	
	
	 public ShelfCommand(ShelfController shelfController) {
	        this.shelfController = shelfController;
	 }

	@Override
	public void execute() {
		shelfController.manageShelf();
	}

}
