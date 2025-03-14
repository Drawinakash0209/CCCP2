package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cccp.command.Command;
import cccp.command.OnlinePurchaseCommand;
import cccp.factory.CustomerControllerFactory;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.OnlineInventoryDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;

class CustomerControllerFactoryTest {


    private BatchDAOInterface batchDAO;
    private BillDAOInterface billDAO;
    private ProductDAOInterface productDAO;
    private SaleDAOInterface saleDAO;
    private OnlineInventoryDAOInterface onlineDAO;
    private CustomerControllerFactory factory;

    @BeforeEach
    void setUp() {
        // Arrange: Mock dependencies
        batchDAO = mock(BatchDAOInterface.class);
        billDAO = mock(BillDAOInterface.class);
        productDAO = mock(ProductDAOInterface.class);
        saleDAO = mock(SaleDAOInterface.class);
        onlineDAO = mock(OnlineInventoryDAOInterface.class);
        
        factory = new CustomerControllerFactory(batchDAO, billDAO, productDAO, saleDAO, onlineDAO);
    }

    @Test
    void testGetCommand_WithValidOption_ReturnsOnlinePurchaseCommand() {
        // Act: Call the method under test
        Command command = factory.getCommand(1);

        // Assert: Verify the correct type is returned
        assertNotNull(command, "Command should not be null");
        assertTrue(command instanceof OnlinePurchaseCommand, "Command should be an instance of OnlinePurchaseCommand");
    }

    @Test
    void testGetCommand_WithInvalidOption_ReturnsNull() {
        // Act: Call the method with an invalid option
        Command command = factory.getCommand(99);

        // Assert: Verify the result is null
        assertNull(command, "Command should be null for an invalid option");
    }
}
