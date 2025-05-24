package test.services;
import cccp.model.Category;
import cccp.model.dao.CategoryDAO;
import cccp.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryDAO categoryDAO;

    @Mock
    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryService();

        // Inject mocked CategoryDAO using reflection
        setPrivateField(categoryService, "categoryDAO", categoryDAO);
    }

    // Helper method to set private final field via reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testGetAllCategories_Success() {
        // Arrange
        List<Category> expectedCategories = List.of(category);
        when(categoryDAO.viewAllItemsGUI()).thenReturn(expectedCategories);

        // Act
        List<Category> result = categoryService.getAllCategories();

        // Assert
        assertEquals(expectedCategories, result);
        verify(categoryDAO).viewAllItemsGUI();
    }

    @Test
    void testGetAllCategories_EmptyList() {
        // Arrange
        List<Category> expectedCategories = List.of();
        when(categoryDAO.viewAllItemsGUI()).thenReturn(expectedCategories);

        // Act
        List<Category> result = categoryService.getAllCategories();

        // Assert
        assertTrue(result.isEmpty());
        verify(categoryDAO).viewAllItemsGUI();
    }


    @Test
    void testCreateCategory_EmptyName_ThrowsIllegalArgumentException() {
        // Arrange
        String categoryName = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            categoryService.createCategory(categoryName),
            "Category name is required."
        );
        verifyNoInteractions(categoryDAO);
    }

    @Test
    void testCreateCategory_DAOFailure_ThrowsException() {
        // Arrange
        String categoryName = "Electronics";
        when(categoryDAO.addItem(any(Category.class))).thenReturn(0);

        // Act & Assert
        assertThrows(Exception.class, () -> 
            categoryService.createCategory(categoryName),
            "Failed to create category."
        );
        verify(categoryDAO).addItem(any(Category.class));
    }

    @Test
    void testUpdateCategory_Success() throws Exception {
        // Arrange
        int id = 1;
        String categoryName = "Electronics";
        when(categoryDAO.searchCategory(id)).thenReturn(category);
        doNothing().when(categoryDAO).updateItem(any(Category.class));

        // Act
        categoryService.updateCategory(id, categoryName);

        // Assert
        verify(categoryDAO).searchCategory(id);
        verify(categoryDAO).updateItem(argThat(c -> 
            c.getId() == id && c.getName().equals(categoryName)
        ));
    }

    @Test
    void testUpdateCategory_NonexistentId_ThrowsIllegalArgumentException() {
        // Arrange
        int id = 1;
        String categoryName = "Electronics";
        when(categoryDAO.searchCategory(id)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            categoryService.updateCategory(id, categoryName),
            "Category with ID 1 not found."
        );
        verify(categoryDAO).searchCategory(id);
        verifyNoMoreInteractions(categoryDAO);
    }


    @Test
    void testDeleteCategory_Success() throws Exception {
        // Arrange
        int id = 1;
        when(categoryDAO.searchCategory(id)).thenReturn(category);
        doNothing().when(categoryDAO).removeItem(id);

        // Act
        categoryService.deleteCategory(id);

        // Assert
        verify(categoryDAO).searchCategory(id);
        verify(categoryDAO).removeItem(id);
    }

    @Test
    void testDeleteCategory_NonexistentId_ThrowsIllegalArgumentException() {
        // Arrange
        int id = 1;
        when(categoryDAO.searchCategory(id)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            categoryService.deleteCategory(id),
            "Category with ID 1 not found."
        );
        verify(categoryDAO).searchCategory(id);
        verifyNoMoreInteractions(categoryDAO);
    }

    @Test
    void testGetCategoryById_Success() throws Exception {
        // Arrange
        int id = 1;
        when(categoryDAO.searchCategory(id)).thenReturn(category);
        when(category.getId()).thenReturn(id);
        when(category.getName()).thenReturn("Electronics");

        // Act
        Category result = categoryService.getCategoryById(id);

        // Assert
        assertEquals(category, result);
        verify(categoryDAO).searchCategory(id);
    }

    @Test
    void testGetCategoryById_NonexistentId_ThrowsIllegalArgumentException() {
        // Arrange
        int id = 1;
        when(categoryDAO.searchCategory(id)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            categoryService.getCategoryById(id),
            "Category with ID 1 not found."
        );
        verify(categoryDAO).searchCategory(id);
    }

    @Test
    void testCreateCategory_DAOException_Propagates() throws Exception {
        // Arrange
        String categoryName = "Electronics";
        doThrow(new RuntimeException("Database error")).when(categoryDAO).addItem(any(Category.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            categoryService.createCategory(categoryName),
            "Database error"
        );
        verify(categoryDAO).addItem(any(Category.class));
    }
}
