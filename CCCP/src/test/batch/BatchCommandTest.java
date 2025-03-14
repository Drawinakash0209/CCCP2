package test.batch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import cccp.command.BatchCommand;
import cccp.controller.BatchController;

public class BatchCommandTest {

    @Test
    public void testExecute() {
        // Arrange
        BatchController mockBatchController = mock(BatchController.class);
        BatchCommand batchCommand = new BatchCommand(mockBatchController);

        // Act
        batchCommand.execute();

        // Assert
        verify(mockBatchController).run(); 
    }

}
