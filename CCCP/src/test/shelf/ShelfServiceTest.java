package test.shelf;

import static org.junit.jupiter.api.Assertions.*;
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

import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.ShelfDAOInterface;
import cccp.service.ShelfService;
import cccp.strategy.BatchSelectionStrategy;

class ShelfServiceTest {

    @Mock
    private BatchDAOInterface batchDAO;

    @Mock
    private ShelfDAOInterface shelfDAO;

    @Mock
    private BatchSelectionStrategy batchSelectionStrategy;

    private ShelfService shelfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shelfService = new ShelfService(batchDAO, shelfDAO);
    }

    @Test
    void testRestockShelf() {
        // Arrange
        String productId = "P001";
        int quantity = 10;
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + 86400000L); // Some expiry date for testing
        List<Batch> batches = Arrays.asList(new Batch("B001", 5, currentDate, expiryDate));
        
        when(batchDAO.getBatchesByProductId(productId)).thenReturn(batches);
        when(batchSelectionStrategy.selectBatch(batches, quantity, currentDate)).thenReturn(batches);

        // Act
        shelfService.restockShelf(productId, quantity, currentDate, batchSelectionStrategy);

        // Assert
        verify(batchDAO, times(1)).getBatchesByProductId(productId);
        verify(batchSelectionStrategy, times(1)).selectBatch(batches, quantity, currentDate);
        verify(shelfDAO, times(1)).addToShelf(productId, 5);
    }

    @Test
    void testReduceShelf() {
        // Arrange
        String productCode = "P001";
        int quantity = 5;

        // Act
        shelfService.reduceShelf(productCode, quantity);

        // Assert
        verify(shelfDAO, times(1)).reduceShelfQuantity(productCode, quantity);
    }
}
