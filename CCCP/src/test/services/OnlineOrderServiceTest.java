package test.services;
import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.OnlineInventoryDAOInterface;
import cccp.service.OnlineOrderService;
import cccp.strategy.BatchSelectionStrategy;
import cccp.ShelfRestockListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class OnlineOrderServiceTest {

    private OnlineOrderService onlineOrderService;

    @Mock
    private BatchDAOInterface batchDAO;

    @Mock
    private OnlineInventoryDAOInterface onlineInventoryDAO;

    @Mock
    private BatchSelectionStrategy strategy;

    @Mock
    private ShelfRestockListener listener;

    @Mock
    private Batch batch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        onlineOrderService = new OnlineOrderService(batchDAO, onlineInventoryDAO);
    }

    @Test
    void testAddRestockListener_Success() {
        // Act
        onlineOrderService.addRestockListener(listener);

        // Assert
        // No direct way to verify list contents without reflection.
        // Verify listener interaction in allocateStockForOnlineOrder tests.
    }

    @Test
    void testAllocateStockForOnlineOrder_Success() {
        // Arrange
        String productId = "P001";
        int quantity = 100;
        Date currentDate = new Date();
        List<Batch> batches = List.of(batch);
        List<Batch> selectedBatches = List.of(batch);
        when(batchDAO.getBatchesByProductId(productId)).thenReturn(batches);
        when(strategy.selectBatch(batches, quantity, currentDate)).thenReturn(selectedBatches);
        when(batch.getQuantity()).thenReturn(50);
        onlineOrderService.addRestockListener(listener);

        // Act
        onlineOrderService.allocateStockForOnlineOrder(productId, quantity, currentDate, strategy);

        // Assert
        verify(batchDAO).getBatchesByProductId(productId);
        verify(strategy).selectBatch(batches, quantity, currentDate);
        verify(onlineInventoryDAO).addToOnlineInventory(productId, 50);
        verify(listener).onShelfRestocked(productId);
    }





    @Test
    void testReduceShelf_Success() {
        // Arrange
        String productCode = "P001";
        int quantity = 10;

        // Act
        onlineOrderService.reduceShelf(productCode, quantity);

        // Assert
        verify(onlineInventoryDAO).reduceOnlineQuantity(productCode, quantity);
        verifyNoInteractions(batchDAO, strategy, listener);
    }

}