package com.utcn.demo.service;

import com.utcn.demo.dto.Mappers.TopicMapper;
import com.utcn.demo.dto.Mappers.UserMapper;
import com.utcn.demo.dto.Requests.UserRequestDTO;
import com.utcn.demo.dto.Responses.UserResponseDTO;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

// @Service — marchează clasa ca un bean Spring de tip "service".
// Spring o detectează automat la pornire și o face disponibilă pentru injecție (Dependency Injection).
// Rolul unui Service: conține LOGICA DE BUSINESS (regulile aplicației),
// și face legătura între Controller (care primește request-uri HTTP) și Repository (care accesează baza de date).
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final TopicMapper topicMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null)
            return null;
        User user = userMapper.toEntity(userRequestDTO);
        return userMapper.toResponse(userRepository.save(user));
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


//    @Transactional
//    public UserResponseDTO updateUser(Long id, UserRequestDTO userDetails, Long currentUserId) {
//        if (!Objects.equals(id, currentUserId))
//            throw new RuntimeException("Nu poți edita contul altui utilizator");
//
//        Predicate<String> checkForUsername = UserService::isValid;
//        Predicate<String> checkForEmail = UserService::isValidEmail;
//        Predicate<String> checkForPassword = UserService::isValidPassword;
//
//        Function<UserRequestDTO, UserResponseDTO> returnUpdatedUser = (current) -> {
//            //luăm user-ul din baza de date nu facem new User() ca să nu pierdem scorul/id-ul!
//            User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//
//            // dacă trece regex-ul (care verifică și != null), modifică.
//            // dacă pică regex-ul pur și simplu nu execută if-ul și trece la următorul
//            if (checkForPassword.test(current.userPassword()))
//                existingUser.setPasswordHash(passwordEncoder.encode(current.userPassword()));
//
//            if (checkForEmail.test(current.userEmail()))
//                existingUser.setEmail(current.userEmail());
//
//            if (checkForUsername.test(current.userName()))
//                existingUser.setUsername(current.userName());
//
//            return userMapper.toResponse(userRepository.save(existingUser));
//        };
//
//        return returnUpdatedUser.apply(userDetails);
//    }

    //TODO revino si refa functia asta dupa modelul cu lambda ,fara Function
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDetails, Long currentUserId) {
        // 1. Verificare securitate
        if (!Objects.equals(id, currentUserId)) {
            throw new RuntimeException("Nu poți edita contul altui utilizator");
        }

        // 2. Lookup & Update (Pattern-ul return apply(DTO))
        return userRepository.findById(id)
                .map(existingUser -> applyUpdates(existingUser, userDetails))
                .map(userRepository::save)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Metodă helper care încapsulează logica de validare și setare.
     * Aceasta înlocuiește Function<> returnUpdatedUser.
     */
    private User applyUpdates(User existingUser, UserRequestDTO dto) {
        // Validare și setare Parolă - Verificăm întâi dacă s-a trimis o parolă (ca să nu stricăm update-urile parțiale)
            if (isValidPassword(dto.userPassword())) {
                existingUser.setPasswordHash(passwordEncoder.encode(dto.userPassword()));
            } else {
                throw new IllegalArgumentException("Eșec validare: Parola trebuie să aibă între 8 și 32 de caractere, să conțină o literă mică, o literă mare, o cifră și un caracter special (@#$%^&+=!).");
            }


        // Validare și setare Email
            if (isValidEmail(dto.userEmail())) {
                existingUser.setEmail(dto.userEmail());
            } else {
                throw new IllegalArgumentException("Eșec validare: Adresa de email nu respectă un format valid (ex. nume@domeniu.com).");
            }


        // Validare și setare Username
            if (isValid(dto.userName())) {
                existingUser.setUsername(dto.userName());
            } else {
                throw new IllegalArgumentException("Eșec validare: Username-ul trebuie să aibă între 5 și 32 de caractere și să conțină doar caractere alfanumerice.");
            }


        return existingUser;
    }
    // TODO come back to learn about pre compiled regex for better performance and
    // regex creation and matching rules
    private static boolean isValid(String input) {
        // Returns true if the string matches the pattern exactly
        return input != null && input.matches("^[a-zA-Z0-9]{5,32}$");
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(regex);
    }

    private static boolean isValidPassword(String password) {
        // Note: You must escape backslashes in Java strings
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,32}$";
        return password != null && password.matches(regex);
    }
}
