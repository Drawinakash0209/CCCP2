package cccp.controller;

import cccp.model.Category;
import cccp.model.dao.CategoryDAO;
import cccp.view.CategoryView;

public class CategoryController {
	private final CategoryView view;
	private final CategoryDAO dao;
	
		public CategoryController(CategoryView view, CategoryDAO dao) {
			this.view = view;
			this.dao = dao;		
		}
		
		public void run() {
			while(true) {
				int choice = view.showMenuAndUserChoice();
				
				switch(choice) {
				case 1 -> {
					Category category = view.getCategoryDetails();
					dao.addItem(category);			
				}
				case 2 -> {
					dao.viewAllItems();
				}
				
				case 3 -> {
					int id  = view.getCategoryId();
					String newName = view.getUpdatedName();
					dao.updateItem(new Category(id, newName));
				}
				
				case 4 ->{
					int id = view.getCategoryId();
					dao.removeItem(id);
				}
				
				case 5 ->{
					System.out.println("Exiting");
					return;
				}
				default -> System.out.println("Invalid entry");
				}
			}
		}

}
