package test.authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cccp.CustomerAuthentication;
import cccp.model.User;

class CustomerAuthenticationTest {


    @Mock
    private User user;

    private CustomerAuthentication customerAuthentication;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        customerAuthentication = new CustomerAuthentication();
    }

    @Test
    void testAuthentication_Success() {
        // Arrange
        when(user.getUserType()).thenReturn("Customer");
        when(user.getPassword()).thenReturn("customer123");

        // Act
        boolean result = customerAuthentication.authentication(user, "customer123");

        // Assert
        assertTrue(result);
        verify(user, times(1)).getUserType();
        verify(user, times(1)).getPassword();
    }

    @Test
    void testAuthentication_Failure_WrongPassword() {
        // Arrange
        when(user.getUserType()).thenReturn("Customer");
        when(user.getPassword()).thenReturn("customer123");

        // Act
        boolean result = customerAuthentication.authentication(user, "wrongPassword");

        // Assert
        assertFalse(result);
        verify(user, times(1)).getUserType();
        verify(user, times(1)).getPassword();
    }

    @Test
    void testAuthentication_Failure_WrongUserType() {
        // Arrange
        when(user.getUserType()).thenReturn("Employee");
        when(user.getPassword()).thenReturn("customer123");

        // Act
        boolean result = customerAuthentication.authentication(user, "customer123");

        // Assert
        assertFalse(result);
        verify(user, times(1)).getUserType();
        verify(user, never()).getPassword();
    }
}
