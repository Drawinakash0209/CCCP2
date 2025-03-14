package test.bill;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.controller.BillController;
import cccp.model.dao.ProductDAOInterface;
import cccp.service.BillingServiceInterface;
import cccp.view.BillView;

class BillControllerTest {
	
	
	 	@Mock
	    private BillView mockView;

	    @Mock
	    private BillingServiceInterface mockBillingService;

	    @Mock
	    private ProductDAOInterface mockProductDao;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.initMocks(this);  // Manually initialize mocks before each test
	    }

	    @Test
	    void run_WithValidChoice_ShouldProcessBillDetails() {
	        // Arrange
	        when(mockView.showMenuAndUserChoice()).thenReturn(1);
	        BillController controller = new BillController(mockView, mockBillingService, mockProductDao);

	        // Act
	        controller.run();

	        // Assert
	        verify(mockView).getBillDetails(mockBillingService, mockProductDao);
	    }

	    @Test
	    void run_WithInvalidChoice_ShouldNotProcessDetails() {
	        // Arrange
	        when(mockView.showMenuAndUserChoice()).thenReturn(999);
	        BillController controller = new BillController(mockView, mockBillingService, mockProductDao);

	        // Act
	        controller.run();

	        // Assert
	        verify(mockView, never()).getBillDetails(any(), any());
	    }

	

}
