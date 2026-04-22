package com.courseselling.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.courseselling.model.User;
import com.courseselling.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("password123");
        testUser.setPhone("1234567890");
        testUser.setAddress("123 Main St");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        testUser.setIsActive(true);
    }

    @Test
    @DisplayName("Should get all users successfully")
    void testGetAllUsers() {
        // Arrange
        User user2 = new User();
        user2.setUserId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane@example.com");

        List<User> userList = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void testGetUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty optional when user not found by ID")
    void testGetUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get user by email successfully")
    void testGetUserByEmail() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(testUser);

        // Act
        User result = userService.getUserByEmail("john@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should register new user successfully")
    void testRegisterUser() {
        // Arrange
        User newUser = new User();
        newUser.setFirstName("Alice");
        newUser.setLastName("Johnson");
        newUser.setEmail("alice@example.com");
        newUser.setPassword("pass123");

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.registerUser(newUser);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should update existing user successfully")
    void testUpdateUserSuccess() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setFirstName("Jonathan");
        updatedUser.setEmail("john@example.com");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should return null when updating non-existent user")
    void testUpdateUserNotFound() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setFirstName("Jonathan");

        when(userRepository.existsById(999L)).thenReturn(false);

        // Act
        User result = userService.updateUser(999L, updatedUser);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).existsById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() {
        // Arrange & Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should authenticate user with correct password")
    void testAuthenticateUserSuccess() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(testUser);

        // Act
        boolean result = userService.authenticateUser("john@example.com", "password123");

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should fail authentication with incorrect password")
    void testAuthenticateUserIncorrectPassword() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(testUser);

        // Act
        boolean result = userService.authenticateUser("john@example.com", "wrongpassword");

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should fail authentication when user not found")
    void testAuthenticateUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act
        boolean result = userService.authenticateUser("nonexistent@example.com", "password123");

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
