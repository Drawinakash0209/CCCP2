package cccp.service;

import java.util.List;
import cccp.model.Category;
import cccp.model.dao.CategoryDAO;

public class CategoryService {
    private final CategoryDAO categoryDAO = new CategoryDAO();

    // View all categories
    public List<Category> getAllCategories() {
        return categoryDAO.viewAllItemsGUI();
    }

    // Create a category
    public int createCategory(String categoryName) throws Exception {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        Category category = new Category(0, categoryName);
        int result = categoryDAO.addItem(category);
        if (result == 0) {
            throw new Exception("Failed to create category.");
        }
        return category.getId(); // Return the generated ID
    }

    // Update a category
    public void updateCategory(int id, String categoryName) throws Exception {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        if (categoryDAO.searchCategory(id) == null) {
            throw new IllegalArgumentException("Category with ID " + id + " not found.");
        }
        Category category = new Category(id, categoryName);
        categoryDAO.updateItem(category);
    }

    // Delete a category
    public void deleteCategory(int id) throws Exception {
        if (categoryDAO.searchCategory(id) == null) {
            throw new IllegalArgumentException("Category with ID " + id + " not found.");
        }
        categoryDAO.removeItem(id);
    }

    // Get a category by ID
    public Category getCategoryById(int id) throws Exception {
        Category category = categoryDAO.searchCategory(id);
        if (category == null) {
            throw new IllegalArgumentException("Category with ID " + id + " not found.");
        }
        return category;
    }
}