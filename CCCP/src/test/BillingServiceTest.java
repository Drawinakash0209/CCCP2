package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.model.Bill;
import cccp.model.Product;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;
import cccp.service.BillingService;
import cccp.service.BillingServiceInterface;
import cccp.service.ShelfServiceInterface;


class BillingServiceTest {

    @Mock private BillDAOInterface mockBillDAO;
    @Mock private ProductDAOInterface mockProductDAO;
    @Mock private ShelfServiceInterface mockShelfService;
    @Mock private SaleDAOInterface mockSaleDAO;

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billingService = new BillingService(mockBillDAO, mockProductDAO, mockShelfService, mockSaleDAO);
    }

    @Test
    void createBill_ShouldSaveBillAndReduceStock() {
        // Arrange
    	Bill.BillItem item1 = Bill.BillItemFactory.createBillItem("P1", "Product1", 2, 10.0, 20.0);
    	Bill.BillItem item2 = Bill.BillItemFactory.createBillItem("P2", "Product2", 1, 15.0, 15.0);

        List<Bill.BillItem> items = Arrays.asList(item1, item2);
        
        when(mockBillDAO.getNextBillId()).thenReturn(1);

        // Act
        Bill result = billingService.createBill(items, 40.0);

        // Assert
        assertEquals(35.0, result.getTotalPrice());
        assertEquals(40.0, result.getCashTendered());
        assertEquals(5.0, result.getChangeAmount());

        verify(mockBillDAO, times(1)).addBill(any(Bill.class));
        verify(mockShelfService, times(1)).reduceShelf("P1", 2);
        verify(mockShelfService, times(1)).reduceShelf("P2", 1);
    }

    @Test
    void createBillItem_ShouldCalculateTotalPrice() {
        // Arrange
    	Product mockProduct = new Product.Builder()
    	        .setId("P1")
    	        .setName("Product1")
    	        .setPrice(10.0)
    	        .setCategoryId(1)  // Assuming some category ID
    	        .setQuantity(50)
    	        .build();

        when(mockProductDAO.getProductById("P1")).thenReturn(mockProduct);

        // Act
        Bill.BillItem item = billingService.createBillItem("P1", 3);

        // Assert
        assertEquals("P1", item.getProductCode());
        assertEquals(3, item.getQuantity());
        assertEquals(30.0, item.getTotalPrice());
    }
}
