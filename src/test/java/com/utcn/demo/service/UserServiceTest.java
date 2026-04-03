package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import com.utcn.demo.dto.UserDTO;
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

        UserDTO savedUserDTO = userService.registerUser(user);

        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(BigDecimal.ZERO, user.getScore());
        verify(userRepository).save(user);
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().username());
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete_WhenCurrentUserIsOwner() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L, 1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenCurrentUserIsNotOwner() {
        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L, 2L));
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateUser_ShouldUpdateDetailsAndSave_WhenCurrentUserIsOwner() {
        User updatedDetails = new User();
        updatedDetails.setUsername("newuser");
        updatedDetails.setPasswordHash("newRawPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newRawPassword")).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUser(1L, updatedDetails, 1L);

        assertEquals("newuser", result.username());
        assertEquals("newHashedPassword", user.getPasswordHash());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenCurrentUserIsNotOwner() {
        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, new User(), 2L));
    }
}
