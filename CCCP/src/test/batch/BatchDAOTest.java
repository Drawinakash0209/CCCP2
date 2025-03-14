package test.batch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.model.Batch;
import cccp.model.dao.BatchDAO;
import cccp.model.dao.BatchDAOInterface;

class BatchDAOTest {

    @Test
    public void testGetBatchesByProductId() {
        // Arrange
        BatchDAOInterface mockDAO = mock(BatchDAOInterface.class);
        Batch batch = new Batch("B001", 10, new Date(), new Date());
        when(mockDAO.getBatchesByProductId("P123")).thenReturn(List.of(batch));

        // Act
        List<Batch> batches = mockDAO.getBatchesByProductId("P123");

        // Assert
        assertEquals(1, batches.size());
        assertEquals("B001", batches.get(0).getBatchcode());
    }
}
