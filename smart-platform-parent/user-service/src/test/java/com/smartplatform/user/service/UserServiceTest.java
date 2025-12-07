package com.smartplatform.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.smartplatform.user.model.Role;
import com.smartplatform.user.model.User;
import com.smartplatform.user.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
                "testuser", "password", "test@example.com", Set.of(Role.CLIENT), "tenant1");
    }

    @Test
    void register_NewUser_ReturnsTrue() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        boolean result = userService.register(
                "testuser", "password", "test@example.com", Set.of(Role.CLIENT), "tenant1");

        assertTrue(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ExistingUsername_ReturnsFalse() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        boolean result = userService.register(
                "testuser",
                "password",
                "other@example.com",
                Set.of(Role.CLIENT),
                "tenant1");

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_ExistingEmail_ReturnsFalse() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        boolean result = userService.register(
                "testuser", "password", "test@example.com", Set.of(Role.CLIENT), "tenant1");

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    void login_ValidCredentials_ReturnsUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User result = userService.login("testuser", "password");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void login_InvalidPassword_ReturnsNull() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        User result = userService.login("testuser", "wrongpassword");

        assertNull(result);
    }

    @Test
    void login_UserNotFound_ReturnsNull() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        User result = userService.login("unknown", "password");

        assertNull(result);
    }
}
