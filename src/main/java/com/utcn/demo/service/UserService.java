package com.utcn.demo.service;

import com.utcn.demo.dto.Mappers.TopicMapper;
import com.utcn.demo.dto.Mappers.UserMapper;
import com.utcn.demo.dto.Requests.UserRequestDTO;
import com.utcn.demo.dto.Responses.UserResponseDTO;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final TopicMapper topicMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;


    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null)
            return null;
        User user = userMapper.toEntity(userRequestDTO);
        // Parola nu se salveaza niciodata in clar — hash BCrypt obligatoriu.
        if (userRequestDTO.password() != null) {
            user.setPasswordHash(passwordEncoder.encode(userRequestDTO.password()));
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public Optional<UserResponseDTO> authenticate(String username, String rawPassword) {
        if (username == null || rawPassword == null) return Optional.empty();
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) return Optional.empty();

        User user = userOpt.get();
        String stored = user.getPasswordHash();
        if (stored == null) return Optional.empty();

        if (passwordEncoder.matches(rawPassword, stored)) {
            return Optional.of(userMapper.toResponse(user));
        }
        // Fallback de migrare pentru conturi vechi cu parola in clar — o acceptam o data si o re-salvam hash-uita.
        if (stored.equals(rawPassword)) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            return Optional.of(userMapper.toResponse(user));
        }
        return Optional.empty();
    }


    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserEntityByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().map(userMapper::toResponse).collect(Collectors.toList());
    }


    public void deleteUser(Long userId, Long currentUserId) {
        if (Objects.equals(userId, currentUserId))
            throw new RuntimeException("You can't delete this user");
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserResponseDTO setBanStatus(Long targetUserId, boolean banned, Long requesterId) {
        if (!isModerator(requesterId)) {
            throw new RuntimeException("Doar un moderator poate bana sau debana utilizatori");
        }
        if (Objects.equals(targetUserId, requesterId)) {
            throw new RuntimeException("Nu te poți bana pe tine însuți");
        }
        return userRepository.findById(targetUserId)
                .map(user -> {
                    user.setIsBanned(banned);
                    User saved = userRepository.save(user);
                    if (banned) {
                        notificationService.notifyUserBanned(saved);
                    }
                    return saved;
                })
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public boolean isModerator(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> "MODERATOR".equals(role.getRoleName())))
                .orElse(false);
    }


    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDetails, Long currentUserId) {
        if (!Objects.equals(id, currentUserId)) {
            throw new RuntimeException("Nu poți edita contul altui utilizator");
        }

        return userRepository.findById(id)
                .map(existingUser -> applyUpdates(existingUser, userDetails))
                .map(userRepository::save)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private User applyUpdates(User existingUser, UserRequestDTO dto) {

        if (dto.password() != null && !dto.password().trim().isEmpty()) {
            if (isValidPassword(dto.password())) {
                existingUser.setPasswordHash(passwordEncoder.encode(dto.password()));
            } else {
                throw new IllegalArgumentException("Eșec validare: Parola trebuie să aibă minim 6 caractere, să conțină o literă mică, o literă mare, o cifră și un caracter special (@#$%^&+=!).");
            }
        }

        if (dto.email() != null && !dto.email().trim().isEmpty()) {
            if (isValidEmail(dto.email())) {
                existingUser.setEmail(dto.email());
            } else {
                throw new IllegalArgumentException("Eșec validare: Adresa de email nu respectă un format valid (ex. nume@domeniu.com).");
            }
        }

        if (dto.username() != null && !dto.username().trim().isEmpty()) {
            if (isValid(dto.username())) {
                existingUser.setUsername(dto.username());
            } else {
                throw new IllegalArgumentException("Eșec validare: Username-ul trebuie să aibă între 5 și 32 de caractere și să conțină doar caractere alfanumerice.");
            }
        }

        return existingUser;
    }

    private static boolean isValid(String input) {
        return input != null && input.matches("^[a-zA-Z0-9]{5,32}$");
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(regex);
    }

    private static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,32}$";
        return password != null && password.matches(regex);
    }
}
