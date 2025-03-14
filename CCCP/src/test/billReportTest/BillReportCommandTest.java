package test.billReportTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.BillReport;
import cccp.ReportService;
import cccp.command.BillReportCommand;
import cccp.model.dao.BillDAOInterface;

class BillReportCommandTest {
	

    @Mock
    private ReportService reportService;  // Mock the ReportService

    @InjectMocks
    private BillReportCommand billReportCommand;  // The command under test

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAllBillReports_ShouldReturnValidBillReport() {
        // Arrange
        BillReport mockBillReport = mock(BillReport.class);  // Create a mock of BillReport
        when(reportService.generateAllBillReports()).thenReturn(mockBillReport);  // Mock the method call

        // Act
        billReportCommand.execute();  // Call the execute method

        // Assert
        verify(reportService).generateAllBillReports();  // Verify if the method was called
        verify(mockBillReport).display();  // Verify if the display method was called
    }
}
