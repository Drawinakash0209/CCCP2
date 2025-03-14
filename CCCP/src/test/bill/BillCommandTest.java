package test.bill;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.command.BillCommand;
import cccp.controller.BillController;

class BillCommandTest {
	
	@Mock
    private BillController mockBillController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);  // Manually initialize mocks before each test
    }

    @Test
    void execute_ShouldInvokeControllerRun() {
        // Arrange
        BillCommand command = new BillCommand(mockBillController);

        // Act
        command.execute();

        // Assert
        verify(mockBillController).run();  // Verifying that run() method was invoked
    }
	    
}
