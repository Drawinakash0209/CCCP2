package test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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
    private ProductDAOInterface mockProductDAO;

    @InjectMocks
    private BillController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void run_WhenUserSelects_CallsGetBillDetails() {
        when(mockView.showMenuAndUserChoice()).thenReturn(1);
        controller.run();
        verify(mockView).getBillDetails(mockBillingService, mockProductDAO);
    }
}
