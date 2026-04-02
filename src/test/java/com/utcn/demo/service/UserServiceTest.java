package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPasswordHash("rawPassword");
    }

    @Test
    void registerUser_ShouldHashPasswordAndSave() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(user);

        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(BigDecimal.ZERO, user.getScore());
        verify(userRepository).save(user);
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void updateUser_ShouldUpdateDetailsAndSave() {
        User updatedDetails = new User();
        updatedDetails.setUsername("newuser");
        updatedDetails.setPasswordHash("newRawPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newRawPassword")).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, updatedDetails);

        assertEquals("newuser", result.getUsername());
        assertEquals("newHashedPassword", result.getPasswordHash());
        verify(userRepository).save(user);
    }
}
