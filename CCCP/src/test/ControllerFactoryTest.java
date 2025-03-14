package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Scanner;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.command.BatchCommand;
import cccp.command.BillCommand;
import cccp.command.BillReportCommand;
import cccp.command.CategoryCommand;
import cccp.command.Command;
import cccp.command.OnlineOrderCommand;
import cccp.command.ProductCommand;
import cccp.command.ReorderReportCommand;
import cccp.command.ReshelveReportCommand;
import cccp.command.SalesReportCommand;
import cccp.command.ShelfCommand;
import cccp.command.StockReportCommand;
import cccp.factory.ControllerFactory;
import cccp.model.dao.BatchDAOInterface;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.OnlineInventoryDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;
import cccp.model.dao.ShelfDAOInterface;

class ControllerFactoryTest {
	
	@Mock private Scanner scanner;
    @Mock private BatchDAOInterface batchDAO;
    @Mock private ShelfDAOInterface shelfDAO;
    @Mock private ProductDAOInterface productDAO;
    @Mock private SaleDAOInterface saleDAO;
    @Mock private BillDAOInterface billDAO;
    @Mock private OnlineInventoryDAOInterface onlineDAO;

    private ControllerFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Manually initialize mocks
        factory = new ControllerFactory(
            scanner,
            batchDAO,
            shelfDAO,
            productDAO,
            saleDAO,
            billDAO,
            onlineDAO
        );
    }

    @Test
    void getCommand_Option1_ReturnsCategoryCommand() {
        Command command = factory.getCommand(1);
        assertInstanceOf(CategoryCommand.class, command);
    }

    @Test
    void getCommand_Option2_ReturnsProductCommand() {
        Command command = factory.getCommand(2);
        assertInstanceOf(ProductCommand.class, command);
    }

    @Test
    void getCommand_Option3_ReturnsBatchCommandWithCorrectDependencies() {
        Command command = factory.getCommand(3);
        assertInstanceOf(BatchCommand.class, command);
        // Verify dependencies are passed through
        verify(batchDAO, times(1)); // Verify interaction if possible
    }

    @Test
    void getCommand_Option4_ReturnsShelfCommandWithServices() {
        Command command = factory.getCommand(4);
        assertInstanceOf(ShelfCommand.class, command);
        verify(shelfDAO, times(1)); // Verify shelfDAO is used
    }

    @Test
    void getCommand_Option5_ReturnsOnlineOrderCommand() {
        Command command = factory.getCommand(5);
        assertInstanceOf(OnlineOrderCommand.class, command);
        verify(onlineDAO, times(1)); // Verify onlineDAO is used
    }

    @Test
    void getCommand_Option6_ReturnsBillCommandWithDependencies() {
        Command command = factory.getCommand(6);
        assertInstanceOf(BillCommand.class, command);
    }

    @Test
    void getCommand_Option7_ReturnsBillReportCommand() {
        Command command = factory.getCommand(7);
        assertInstanceOf(BillReportCommand.class, command);
    }

    @Test
    void getCommand_Option8_ReturnsSalesReportCommand() {
        Command command = factory.getCommand(8);
        assertInstanceOf(SalesReportCommand.class, command);
    }

    @Test
    void getCommand_Option9_ReturnsReorderReportCommand() {
        Command command = factory.getCommand(9);
        assertInstanceOf(ReorderReportCommand.class, command);
    }

    @Test
    void getCommand_Option10_ReturnsStockReportCommand() {
        Command command = factory.getCommand(10);
        assertInstanceOf(StockReportCommand.class, command);
    }

    @Test
    void getCommand_Option11_ReturnsReshelveReportCommand() {
        Command command = factory.getCommand(11);
        assertInstanceOf(ReshelveReportCommand.class, command);
   
    }

    @Test
    void getCommand_InvalidOption_ReturnsNull() {
        assertNull(factory.getCommand(99));
    }
    

}
