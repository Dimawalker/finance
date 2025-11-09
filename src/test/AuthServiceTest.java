package com.finance;

import com.finance.repository.UserRepository;
import com.finance.service.AuthService;
import com.finance.repository.JsonDataManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    void testRegistration() {
        JsonDataManager jsonManager = new JsonDataManager();
        UserRepository userRepository = new UserRepository(jsonManager);
        AuthService authService = new AuthService(userRepository);

        assertTrue(authService.register("newuser", "password"));
        assertFalse(authService.register("newuser", "password2")); // дубликат
    }

    @Test
    void testLogin() {
        JsonDataManager jsonManager = new JsonDataManager();
        UserRepository userRepository = new UserRepository(jsonManager);
        AuthService authService = new AuthService(userRepository);

        authService.register("testuser", "password");

        assertTrue(authService.login("testuser", "password"));
        assertFalse(authService.login("testuser", "wrongpassword"));
        assertFalse(authService.login("nonexistent", "password"));
    }

    @Test
    void testAuthentication() {
        JsonDataManager jsonManager = new JsonDataManager();
        UserRepository userRepository = new UserRepository(jsonManager);
        AuthService authService = new AuthService(userRepository);

        assertFalse(authService.isAuthenticated());

        authService.register("user", "pass");
        authService.login("user", "pass");

        assertTrue(authService.isAuthenticated());
        assertNotNull(authService.getCurrentUser());
    }
}