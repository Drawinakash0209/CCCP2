package test.shelf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import cccp.controller.ShelfController;
import cccp.service.ShelfServiceInterface;
import cccp.strategy.BatchSelectionStrategy;
import cccp.strategy.ExpiryBasedSelectionStrategy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ShelfControllerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Mock
    private ShelfServiceInterface shelfService;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private ShelfController shelfController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Manually initialize mocks
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void manageShelf_EmptyProductId_PrintsError() {
        when(scanner.nextLine()).thenReturn("");
        
        shelfController.manageShelf();
        
        assertTrue(outContent.toString().contains("Error: Product ID cannot be empty."));
        verify(shelfService, never()).restockShelf(any(), anyInt(), any(), any());
    }

    @Test
    void manageShelf_InvalidQuantityInput_PrintsError() {
        when(scanner.nextLine())
            .thenReturn("prod123")
            .thenReturn("");
        doThrow(new InputMismatchException()).when(scanner).nextInt();
        
        shelfController.manageShelf();
        
        String output = outContent.toString();
        assertTrue(output.contains("Error: Invalid quantity. Please enter a valid number."));
        verify(shelfService, never()).restockShelf(any(), anyInt(), any(), any());
    }

    @Test
    void manageShelf_NonPositiveQuantity_PrintsError() {
        when(scanner.nextLine())
            .thenReturn("prod123")
            .thenReturn("");
        when(scanner.nextInt()).thenReturn(0);
        
        shelfController.manageShelf();
        
        String output = outContent.toString();
        assertTrue(output.contains("Error: Quantity must be greater than zero."));
        verify(shelfService, never()).restockShelf(any(), anyInt(), any(), any());
    }

    @Test
    void manageShelf_ValidInputs_CallsRestockShelf() {
        when(scanner.nextLine())
            .thenReturn("prod123")
            .thenReturn("");
        when(scanner.nextInt()).thenReturn(5);
        
        shelfController.manageShelf();
        
        ArgumentCaptor<String> productIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> quantityCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<BatchSelectionStrategy> strategyCaptor = ArgumentCaptor.forClass(BatchSelectionStrategy.class);

        verify(shelfService).restockShelf(
            productIdCaptor.capture(),
            quantityCaptor.capture(),
            dateCaptor.capture(),
            strategyCaptor.capture()
        );

        assertEquals("prod123", productIdCaptor.getValue());
        assertEquals(5, quantityCaptor.getValue().intValue());
        assertNotNull(dateCaptor.getValue());
        assertTrue(strategyCaptor.getValue() instanceof ExpiryBasedSelectionStrategy);
        assertTrue(outContent.toString().contains("Shelf restocked successfully."));
    }

    @Test
    void manageShelf_ServiceThrowsException_PrintsError() {
        when(scanner.nextLine())
            .thenReturn("prod123")
            .thenReturn("");
        when(scanner.nextInt()).thenReturn(5);
        doThrow(new IllegalStateException("Insufficient")).when(shelfService)
            .restockShelf(any(), anyInt(), any(), any());
        
        shelfController.manageShelf();
        
        String output = outContent.toString();
        assertTrue(output.contains("Error: Insufficient"));
        verify(shelfService).restockShelf(any(), anyInt(), any(), any());
    }
}

