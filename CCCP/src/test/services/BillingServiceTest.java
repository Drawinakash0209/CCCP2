package test.services;
import cccp.Discount;
import cccp.model.Bill;
import cccp.model.Product;
import cccp.model.Sale;
import cccp.model.dao.BillDAOInterface;
import cccp.model.dao.ProductDAOInterface;
import cccp.model.dao.SaleDAOInterface;
import cccp.service.BillingService;
import cccp.service.OnlineOrderServiceInterface;
import cccp.service.ShelfServiceInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BillingServiceTest {

    private BillingService inStoreBillingService;
    private BillingService onlineBillingService;

    @Mock
    private BillDAOInterface billDAO;

    @Mock
    private ProductDAOInterface productDAO;

    @Mock
    private ShelfServiceInterface shelfService;

    @Mock
    private SaleDAOInterface saleDAO;

    @Mock
    private OnlineOrderServiceInterface onlineOrderService;

    @Mock
    private Discount discount;

    @Mock
    private Bill bill;

    @Mock
    private Bill.BillItem billItem;

    @Mock
    private Product product;

    @Mock
    private Sale sale;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inStoreBillingService = new BillingService(billDAO, productDAO, shelfService, saleDAO);
        onlineBillingService = new BillingService(billDAO, productDAO, onlineOrderService, saleDAO);
    }

    @Test
    void testCreateBill_NoDiscount_Success() {
        // Arrange
        when(billItem.getProductCode()).thenReturn("P001");
        when(billItem.getQuantity()).thenReturn(2);
        when(billItem.getTotalPrice()).thenReturn(2000.0);
        List<Bill.BillItem> billItems = List.of(billItem);
        double cashTendered = 2500.0;
        when(billDAO.getNextBillId()).thenReturn(1);
        when(bill.getBillId()).thenReturn(1);
        when(bill.getTotalPrice()).thenReturn(2000.0);
        when(bill.getCashTendered()).thenReturn(2500.0);
        when(bill.getChangeAmount()).thenReturn(500.0);
        when(bill.getDiscount()).thenReturn(0.0);

        // Act
        Bill result = inStoreBillingService.createBill(billItems, cashTendered);

        // Assert
        assertNotNull(result);
        verify(billDAO).getNextBillId();
        verify(billDAO).addBill(any(Bill.class));
        verify(shelfService).reduceShelf("P001", 2);
        verify(saleDAO).addSale(any(Sale.class));
        verify(saleDAO).addSale(argThat(s -> 
            s.getProductCode().equals("P001") &&
            s.getQuantitySold() == 2 &&
            s.getTotalRevenue() == 2000.0 &&
            s.getSaleType().equals("INSTORE")
        ));
    }


    @Test
    void testCreateOnlineBill_Success() {
        // Arrange
        when(billItem.getProductCode()).thenReturn("P001");
        when(billItem.getQuantity()).thenReturn(2);
        when(billItem.getTotalPrice()).thenReturn(2000.0);
        List<Bill.BillItem> billItems = List.of(billItem);
        double cashTendered = 2000.0;
        when(billDAO.getNextBillId()).thenReturn(1);
        when(bill.getBillId()).thenReturn(1);
        when(bill.getTotalPrice()).thenReturn(2000.0);
        when(bill.getCashTendered()).thenReturn(2000.0);
        when(bill.getChangeAmount()).thenReturn(0.0);

        // Act
        Bill result = onlineBillingService.createOnlineBill(billItems, cashTendered);

        // Assert
        assertNotNull(result);
        verify(billDAO).getNextBillId();
        verify(billDAO).addBill(any(Bill.class));
        verify(onlineOrderService).reduceShelf("P001", 2);
        verify(saleDAO).addSale(any(Sale.class));
        verify(saleDAO).addSale(argThat(s -> 
            s.getProductCode().equals("P001") &&
            s.getQuantitySold() == 2 &&
            s.getTotalRevenue() == 2000.0 &&
            s.getSaleType().equals("ONLINE")
        ));
    }

    @Test
    void testGenerateOnlineBill_Success() {
        // Arrange
        int customerId = 1;
        when(product.getId()).thenReturn("P001");
        when(product.getName()).thenReturn("Laptop");
        when(product.getPrice()).thenReturn(1000.0);
        Map<Product, Integer> items = Map.of(product, 2);
        when(productDAO.getProductById("P001")).thenReturn(product);
        when(billDAO.getNextBillId()).thenReturn(1);
        when(billItem.getProductCode()).thenReturn("P001");
        when(billItem.getQuantity()).thenReturn(2);
        when(billItem.getTotalPrice()).thenReturn(2000.0);
        when(bill.getBillId()).thenReturn(1);
        when(bill.getTotalPrice()).thenReturn(2000.0);
        when(bill.getCashTendered()).thenReturn(2000.0);
        when(bill.getChangeAmount()).thenReturn(0.0);

        // Act
        Bill result = onlineBillingService.generateOnlineBill(customerId, items);

        // Assert
        assertNotNull(result);
        verify(productDAO).getProductById("P001");
        verify(billDAO).getNextBillId();
        verify(billDAO).addBill(any(Bill.class));
        verify(onlineOrderService).reduceShelf("P001", 2);
        verify(saleDAO).addSale(any(Sale.class));
    }

    @Test
    void testCreateBillItem_Success() {
        // Arrange
        String productId = "P001";
        int quantity = 2;
        when(productDAO.getProductById(productId)).thenReturn(product);
        when(product.getPrice()).thenReturn(1000.0);
        when(product.getName()).thenReturn("Laptop");
        when(billItem.getProductCode()).thenReturn("P001");
        when(billItem.getproductName()).thenReturn("Laptop");
        when(billItem.getQuantity()).thenReturn(2);
        when(billItem.getPrice()).thenReturn(1000.0);
        when(billItem.getTotalPrice()).thenReturn(2000.0);

        // Act
        Bill.BillItem result = inStoreBillingService.createBillItem(productId, quantity);

        // Assert
        assertNotNull(result);
        verify(productDAO).getProductById(productId);
        // Note: Bill.BillItemFactory.createBillItem is static and not easily mocked without PowerMock.
        // Assume it returns the mocked billItem for simplicity.
    }

    @Test
    void testCreateBill_DAOException_Propagates() {
        // Arrange
        when(billItem.getProductCode()).thenReturn("P001");
        when(billItem.getQuantity()).thenReturn(2);
        when(billItem.getTotalPrice()).thenReturn(2000.0);
        List<Bill.BillItem> billItems = List.of(billItem);
        double cashTendered = 2500.0;
        when(billDAO.getNextBillId()).thenReturn(1);
        doThrow(new RuntimeException("Database error")).when(billDAO).addBill(any(Bill.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            inStoreBillingService.createBill(billItems, cashTendered)
        );
        verify(billDAO).getNextBillId();
        verify(billDAO).addBill(any(Bill.class));
        verifyNoInteractions(shelfService, saleDAO);
    }
}