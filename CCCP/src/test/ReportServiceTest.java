package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.BillReport;
import cccp.ReportService;
import cccp.model.Bill;
import cccp.model.dao.BillDAOInterface;

class ReportServiceTest {

	@Mock
    private BillDAOInterface billDAO;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService(billDAO);
    }

    @Test
    void testGenerateAllBillReports() {
        // Arrange
        Bill bill1 = new Bill.BillBuilder()
                .setBillId(1)
                .setBillDate(new Date())
                .setTotalPrice(100.0)
                .setCashTendered(150.0)
                .setchangeAmount(50.0)
                .build();

        Bill bill2 = new Bill.BillBuilder()
                .setBillId(2)
                .setBillDate(new Date())
                .setTotalPrice(200.0)
                .setCashTendered(250.0)
                .setchangeAmount(50.0)
                .build();

        List<Bill> mockBills = Arrays.asList(bill1, bill2);
        when(billDAO.generateAllBillReports()).thenReturn(mockBills);

        // Act
        BillReport billReport = reportService.generateAllBillReports();

        // Assert
        assertNotNull(billReport);
        verify(billDAO, times(1)).generateAllBillReports();
    }
    
    
}
