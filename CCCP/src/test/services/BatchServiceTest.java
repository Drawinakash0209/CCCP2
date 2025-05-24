package test.services;

import cccp.model.Batch;
import cccp.model.dao.BatchDAOInterface;
import cccp.service.BatchService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BatchServiceTest {

    private BatchService batchService;

    @Mock
    private BatchDAOInterface batchDAO;

    @Mock
    private Batch batch;

    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        batchService = new BatchService(batchDAO);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Test
    void testAddBatch_Success() throws ParseException {
        // Arrange
        String productId = "P001";
        String batchId = "B001";
        int quantity = 100;
        String purchaseDateStr = "2025-05-24";
        String expiryDateStr = "2026-05-24";

        // Act
        batchService.addBatch(productId, batchId, quantity, purchaseDateStr, expiryDateStr);

        // Assert
        verify(batchDAO).addBatch(any(Batch.class), eq(productId));
        // Verify the Batch object passed to addBatch (optional, for stricter testing)
        verify(batchDAO).addBatch(argThat(b -> 
            {
				try {
					return b.getBatchcode().equals(batchId) &&
					b.getQuantity() == quantity &&
					b.getPurchaseDate().equals(dateFormat.parse(purchaseDateStr)) &&
					b.getExpiryDate().equals(dateFormat.parse(expiryDateStr));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
        ), eq(productId));
    }

    @Test
    void testAddBatch_InvalidDateFormat_ThrowsParseException() {
        // Arrange
        String productId = "P001";
        String batchId = "B001";
        int quantity = 100;
        String purchaseDateStr = "2025/05/24"; // Invalid format
        String expiryDateStr = "2026-05-24";

        // Act & Assert
        assertThrows(ParseException.class, () -> 
            batchService.addBatch(productId, batchId, quantity, purchaseDateStr, expiryDateStr)
        );
        verifyNoInteractions(batchDAO);
    }

    @Test
    void testGetBatchesByProductId_ReturnsBatches() {
        // Arrange
        String productId = "P001";
        List<Batch> expectedBatches = List.of(batch);
        when(batchDAO.getBatchesByProductId(productId)).thenReturn(expectedBatches);

        // Act
        List<Batch> result = batchService.getBatchesByProductId(productId);

        // Assert
        assertEquals(expectedBatches, result);
        verify(batchDAO).getBatchesByProductId(productId);
    }

    @Test
    void testGetBatchesByProductId_EmptyList() {
        // Arrange
        String productId = "P001";
        List<Batch> expectedBatches = List.of();
        when(batchDAO.getBatchesByProductId(productId)).thenReturn(expectedBatches);

        // Act
        List<Batch> result = batchService.getBatchesByProductId(productId);

        // Assert
        assertTrue(result.isEmpty());
        verify(batchDAO).getBatchesByProductId(productId);
    }

    @Test
    void testAddBatch_DAOException_Propagates() throws ParseException {
        // Arrange
        String productId = "P001";
        String batchId = "B001";
        int quantity = 100;
        String purchaseDateStr = "2025-05-24";
        String expiryDateStr = "2026-05-24";
        doThrow(new RuntimeException("Database error")).when(batchDAO)
            .addBatch(any(Batch.class), eq(productId));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            batchService.addBatch(productId, batchId, quantity, purchaseDateStr, expiryDateStr)
        );
        verify(batchDAO).addBatch(any(Batch.class), eq(productId));
    }
}