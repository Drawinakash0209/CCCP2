package test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cccp.controller.ProductController;
import cccp.model.Product;
import cccp.model.dao.ProductDAOInterface;
import cccp.view.ProductView;

public class ProductControllerTest {
	private ProductView view; 
    private ProductDAOInterface dao;
    private ProductController controller;
	
	
 
	  @BeforeEach
	  // Set up mock objects and initialize the controller
	    void setUp() {
	        view = mock(ProductView.class);
	        dao = mock(ProductDAOInterface.class);
	        controller = new ProductController(view, dao);
	    }
	  
	  
	  
	  @Test
	  // Arrange: Create a product and simulate user input for adding a product
	    void testAddProduct() {
	        Product product = new Product.Builder()
	            .setId("P123")
	            .setName("Test Product")
	            .setPrice(100.0)
	            .setCategoryId(1)
	            .build();
	        when(view.showMenuAndUserChoice()).thenReturn(1, 6); // Choice 1 -> Add Product, then Exit
	        when(view.getProductDetails()).thenReturn(product);

	        // Act
	        controller.run();

	        // Assert
	        verify(dao, times(1)).addItem(product);
	        verify(view, times(1)).getProductDetails();
	    }
	  
	  
	  
	  @Test
	  // Arrange: Simulate user input for viewing all products
	    void testViewAllProducts() {
	        when(view.showMenuAndUserChoice()).thenReturn(2, 6); // Choice 2 -> View All Products, then Exit

	        // Act
	        controller.run();

	        // Assert
	        verify(dao, times(1)).viewAllItems();
	    }
	  
	  
	  @Test
	  // Arrange: Simulate user input for searching a product by ID
	    void testSearchProductById() {
	        String productId = "wommala";
	        when(view.showMenuAndUserChoice()).thenReturn(3, 6); // Choice 3 -> Search Product, then Exit
	        when(view.getProductId()).thenReturn(productId);
	        when(dao.searchProductById(productId)).thenReturn(
	            new Product.Builder()
	                .setId(productId)
	                .setName("Test Product")
	                .setPrice(100.0)
	                .setCategoryId(1)
	                .build()
	        );

	        // Act
	        controller.run();

	        // Assert
	        verify(view, times(1)).getProductId();
	        verify(dao, times(1)).searchProductById(productId);
	    }
	  
	  
	  @Test
	// Arrange: Create an updated product and simulate user input for updating it
	    void testUpdateProduct() {
	        Product updatedProduct = new Product.Builder()
	            .setId("P123")
	            .setName("Updated Product")
	            .setPrice(120.0)
	            .setCategoryId(1)
	            .build();
	        when(view.showMenuAndUserChoice()).thenReturn(4, 6); // Choice 4 -> Update Product, then Exit
	        when(view.getUpdatedProductDetails()).thenReturn(updatedProduct);

	        // Act
	        controller.run();

	        // Assert
	        verify(dao, times(1)).updateItem(updatedProduct);
	        verify(view, times(1)).getUpdatedProductDetails();
	    }
	  
	  
	  @Test
	  // Arrange: Simulate user input for removing a product by ID
	    void testRemoveProduct() {
	        String productId = "P123";
	        when(view.showMenuAndUserChoice()).thenReturn(5, 6); // Choice 5 -> Remove Product, then Exit
	        when(view.getProductId()).thenReturn(productId);

	        // Act
	        controller.run();

	        // Assert
	        verify(dao, times(1)).removeItem(productId);
	        verify(view, times(1)).getProductId();
	    }
	  
	  
	  @Test
	  // Arrange: Simulate an invalid menu choice by the user
	  void testInvalidChoice() {
	        when(view.showMenuAndUserChoice()).thenReturn(7, 6); // Invalid Choice, then Exit

	        // Act
	        controller.run();

	        // Assert
	        verifyNoInteractions(dao); // Ensure no DAO operations were performed
	    }
	  
	  
	  
	   
	
	
	
	
	
	
	
	


	
}
