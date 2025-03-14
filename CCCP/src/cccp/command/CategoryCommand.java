package cccp.command;

import cccp.controller.CategoryController;

public class CategoryCommand implements Command {

	private CategoryController categoryController;
	
	public CategoryCommand(CategoryController categoryController) {
		this.categoryController	= categoryController;
	}
	
	@Override
	
	public void execute() {
		categoryController.run();
	}

}
