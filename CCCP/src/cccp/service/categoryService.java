package cccp.service;

import java.util.List;

import cccp.model.Category;
import cccp.model.dao.CategoryDAO;

public class categoryService {
    private final CategoryDAO categoryDAO = new CategoryDAO();

    // View all categories
    public List<Category> getAllCategories() {
        return categoryDAO.viewAllItemsGUI();
    }

    // Create a category
    public int createCategory(String categoryName) {
        Category category = new Category(0, categoryName);
        return categoryDAO.addItem(category);
    }

    // Update a category
    public void updateCategory(int id, String categoryName) {
        Category category = new Category(id, categoryName);
        categoryDAO.updateItem(category);
    }

    // Delete a category
    public void deleteCategory(int id) {
        categoryDAO.removeItem(id);
    }
}
