package test.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import cccp.controller.ProductController;
import cccp.model.Product;
import cccp.model.dao.ProductDAOInterface;
import cccp.view.ProductView;

class ProductControllerTest {

	   private ProductView view;
	    private ProductDAOInterface dao;
	    private ProductController controller;
	    private AutoCloseable closeable;

	    @BeforeEach
	    void setUp() {
	        closeable = MockitoAnnotations.openMocks(this);
	        view = mock(ProductView.class);
	        dao = mock(ProductDAOInterface.class);
	        controller = new ProductController(view, dao);
	    }
	    
	    
	    

	    @AfterEach
	    void tearDown() throws Exception {
	        closeable.close();
	    }
	    
	    
	    @Test
	    void testAddProduct() {
	    	Product mockProduct = new Product.Builder()
	    		    .setId("P1")
	    		    .setName("Product 1")
	    		    .setPrice(100.0)
	    		    .setCategoryId(1)  
	    		    .setReorderLevel(10) 
	    		    .setQuantity(50) 
	    		    .build();
	        when(view.showMenuAndUserChoice()).thenReturn(1, 6);
	        when(view.getProductDetails()).thenReturn(mockProduct);

	        controller.run();

	        verify(dao).addItem(mockProduct);
	    }

	    @Test
	    void testViewAllProducts() {
	        when(view.showMenuAndUserChoice()).thenReturn(2, 6);

	        controller.run();

	        verify(dao).viewAllItems();
	    }
	    
	    
	    @Test
	    void testSearchProductById() {
	        String productId = "P1";
	        when(view.showMenuAndUserChoice()).thenReturn(3, 6);
	        when(view.getProductId()).thenReturn(productId);

	        controller.run();

	        verify(dao).searchProductById(productId);
	    }
	    
	    
	    @Test
	    void testUpdateProduct() {
	    	Product mockProduct = new Product.Builder()
	    		    .setId("P1")
	    		    .setName("Product 1")
	    		    .setPrice(100.0)
	    		    .setCategoryId(1) 
	    		    .setReorderLevel(20)
	    		    .setQuantity(50) 
	    		    .build();
	        when(view.showMenuAndUserChoice()).thenReturn(4, 6);
	        when(view.getUpdatedProductDetails()).thenReturn(mockProduct);

	        controller.run();

	        verify(dao).updateItem(mockProduct);
	    }
	    
	    @Test
	    void testRemoveProduct() {
	        String productId = "P1";
	        when(view.showMenuAndUserChoice()).thenReturn(5, 6);
	        when(view.getProductId()).thenReturn(productId);

	        controller.run();

	        verify(dao).removeItem(productId);
	    }
	    
	    @Test
	    void testExit() {
	        when(view.showMenuAndUserChoice()).thenReturn(6);

	        controller.run();

	        verifyNoInteractions(dao);
	    }
	   

}
