package test.batch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.controller.BatchController;
import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;
import cccp.service.ProductServiceInterface;

import cccp.view.BatchView;

class BatchControllerTest {
	
	@Mock
    private BatchView mockView;
    
    @Mock
    private BatchDAOInterface mockBatchDAO;
    
    @Mock
    private ProductServiceInterface mockProductService;
    
    private BatchController controller;
    
	
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes the mocks
        controller = new BatchController(mockView, mockBatchDAO, mockProductService);
    }
	

    @Test
    public void testAddBatch() {
        // Arrange
        String productId = "P123";
        Batch batch = new Batch("BATCH001", 50, new Date(), new Date());
        when(mockView.showMenuAndUserChoice()).thenReturn(1);
        when(mockView.getProductId()).thenReturn(productId);
        when(mockView.getBatchDetails()).thenReturn(batch);
        
        // Act
        controller.run();
        
        // Assert
        verify(mockProductService, times(1)).addBatchToProduct(productId, batch);
        verify(mockView, times(1)).getProductId();
        verify(mockView, times(1)).getBatchDetails();
    }
    
    @Test
    public void testViewBatches() {
        // Arrange
        String productId = "T000";
        List<Batch> batches = Arrays.asList(
                new Batch("BATCH001", 50, new Date(), new Date()),
                new Batch("BATCH002", 100, new Date(), new Date())
        );
        
        when(mockView.showMenuAndUserChoice()).thenReturn(2);
        when(mockView.getProductId()).thenReturn(productId);
        when(mockBatchDAO.getBatchesByProductId(productId)).thenReturn(batches);
        
        // Act
        controller.run();
        
        // Assert
        verify(mockView, times(1)).viewAllBatches(batches);
        verify(mockView, times(1)).getProductId();
        verify(mockBatchDAO, times(1)).getBatchesByProductId(productId);
    }
    
    @Test
    public void testAddBatchWithZeroOrNegativeQuantity() {
        // Arrange
        String productId = "P123";
        Batch batchWithZeroQuantity = new Batch("BATCH002", 0, new Date(), new Date()); // Invalid quantity
        Batch batchWithNegativeQuantity = new Batch("BATCH003", -10, new Date(), new Date()); // Invalid quantity
        
        when(mockView.showMenuAndUserChoice()).thenReturn(1);
        when(mockView.getProductId()).thenReturn(productId);
        
        // Test with zero quantity
        when(mockView.getBatchDetails()).thenReturn(batchWithZeroQuantity); 
        
        // Act
        controller.run();
        
        // Assert
        verify(mockProductService, times(0)).addBatchToProduct(productId, batchWithZeroQuantity); // Batch should not be added
        
        // Test with negative quantity
        when(mockView.getBatchDetails()).thenReturn(batchWithNegativeQuantity); 
        
        // Act
        controller.run();
        
        // Assert
        verify(mockProductService, times(0)).addBatchToProduct(productId, batchWithNegativeQuantity); // Batch should not be added
    }
    
    
    @Test
    public void testAddBatchWithExpiryDateBeforePurchaseDate() {
        // Arrange
        String productId = "P123";
        Date purchaseDate = new Date();
        Date expiryDate = new Date(purchaseDate.getTime() - 1000); // Expiry date is before the purchase date
        Batch batch = new Batch("BATCH004", 50, purchaseDate, expiryDate);
        
        when(mockView.showMenuAndUserChoice()).thenReturn(1);
        when(mockView.getProductId()).thenReturn(productId);
        when(mockView.getBatchDetails()).thenReturn(batch);
        
        // Act
        controller.run();
        
        // Assert
        verify(mockProductService, times(0)).addBatchToProduct(productId, batch); // The batch should not be added
        verify(mockView, times(1)).getBatchDetails(); 
    }


    


}
