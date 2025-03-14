package test.shelf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.command.ShelfCommand;
import cccp.controller.ShelfController;

class ShelfCommandTest {

    @Mock
    private ShelfController shelfController;

    private ShelfCommand shelfCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shelfCommand = new ShelfCommand(shelfController);
    }

    @Test
    void testExecute() {
        // Act
        shelfCommand.execute();

        // Assert
        verify(shelfController, times(1)).manageShelf();
    }
}
