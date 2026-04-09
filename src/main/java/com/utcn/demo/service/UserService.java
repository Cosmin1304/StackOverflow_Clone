package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.utcn.demo.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        user.setScore(java.math.BigDecimal.ZERO);

        return UserDTO.fromEntity(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserEntityByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
    }

    public void deleteUser(Long userId, Long currentUserId) {
        if (!userId.equals(currentUserId)) {
            throw new RuntimeException("Eroare de permisiune: Puteți șterge doar propriul cont!");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserDTO updateUser(Long id, User userDetails, Long currentUserId) {
        if (!id.equals(currentUserId)) {
            throw new RuntimeException("Eroare de permisiune: Vă puteți edita doar propriul cont!");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDetails.getUsername());

        if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
        }
        return UserDTO.fromEntity(userRepository.save(user));
    }
}
