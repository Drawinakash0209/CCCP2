package test.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import cccp.command.ProductCommand;
import cccp.controller.ProductController;

class ProductCommandTest {

	@Test
    void testExecute() {
        // Arrange
        ProductController mockController = mock(ProductController.class);
        ProductCommand productCommand = new ProductCommand(mockController);
        
        // Act
        productCommand.execute();
        
        // Assert
        verify(mockController, times(1)).run();
    }
}
