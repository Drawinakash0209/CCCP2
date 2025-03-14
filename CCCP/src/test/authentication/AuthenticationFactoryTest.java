package test.authentication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import cccp.CustomerAuthentication;
import cccp.EmployeeAuthentication;
import cccp.factory.AuthenticationFactory;
import cccp.strategy.AuthenticationStrategy;

class AuthenticationFactoryTest {

	@Test
    void testGetAuthenticationStrategy_Employee() {
        // Arrange, Act
        AuthenticationStrategy strategy = AuthenticationFactory.getAuthenticationStrategy("Employee");

        // Assert
        assertNotNull(strategy);
        assertTrue(strategy instanceof EmployeeAuthentication);
    }

    @Test
    void testGetAuthenticationStrategy_Customer() {
        // Arrange, Act
        AuthenticationStrategy strategy = AuthenticationFactory.getAuthenticationStrategy("Customer");

        // Assert
        assertNotNull(strategy);
        assertTrue(strategy instanceof CustomerAuthentication);
    }

    @Test
    void testGetAuthenticationStrategy_InvalidUserType() {
        // Arrange & Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AuthenticationFactory.getAuthenticationStrategy("InvalidType");
        });

        assertEquals("Invalid user", exception.getMessage());
    }
}
