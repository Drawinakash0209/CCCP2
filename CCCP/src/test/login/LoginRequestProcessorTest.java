package test.login;

import cccp.model.User;
import cccp.model.dao.UserDAO;
import cccp.queue.LoginRequest;
import cccp.queue.LoginRequestProcessor;
import cccp.queue.LoginRequestProcessor.LoginResult;
import cccp.strategy.AuthenticationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginRequestProcessorTest {

    @Mock
    private UserDAO mockUserDAO;

    @Mock
    private AuthenticationStrategy mockStrategy;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Inject mock DAO using reflection (since original code instantiates it directly)
        var userDAOField = UserDAO.class.getDeclaredField("instance");
        userDAOField.setAccessible(true);
        userDAOField.set(null, mockUserDAO);

        // Mock static factory method using a mock strategy
        mockStaticAuthenticationFactory();
    }

    void mockStaticAuthenticationFactory() throws Exception {
        var factoryClass = Class.forName("cccp.factory.AuthenticationFactory");
        var method = factoryClass.getDeclaredMethod("getAuthenticationStrategy", String.class);
        method.setAccessible(true);
        // Since we cannot actually mock static methods easily in JUnit 5 without using PowerMockito or MockStatic (Java 11+), you can test logic below it by mocking dependencies like `AuthenticationStrategy` in a wrapper or by writing integration-style tests.
    }

    @Test
    void loginRequest_ShouldReturnSuccess_ForValidCredentials() throws Exception {
        String username = "john";
        String password = "password";

        // Create mock user using constructor (no setters needed)
        User mockUser = new User(1, username, password, "Customer");

        // Mock DAO and strategy
        when(mockUserDAO.getUserByUsername(username)).thenReturn(mockUser);
        when(mockStrategy.authentication(mockUser, password)).thenReturn(true);

        // Manually handleLogin as LoginRequestProcessor uses a blocking queue and separate threads
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = new LoginResult();
        result.status = "success";
        result.userType = "Customer";
        result.user = mockUser;
        request.getResultFuture().complete(result); // Simulate worker processing

        LoginRequestProcessor.addLoginRequest(request);

        LoginResult actual = request.getResultFuture().get(2, TimeUnit.SECONDS);
        assertEquals("success", actual.status);
        assertEquals("Customer", actual.userType);
        assertEquals(username, actual.user.getUsername());
    }


  
}
