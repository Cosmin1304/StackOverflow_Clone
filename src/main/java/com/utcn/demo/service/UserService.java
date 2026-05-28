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
    private final NotificationService notificationService;


    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null)
            return null;
        User user = userMapper.toEntity(userRequestDTO);
        // CRITIC: parola NU se salvează niciodată în clar — o hash-uim cu BCrypt.
        if (userRequestDTO.password() != null) {
            user.setPasswordHash(passwordEncoder.encode(userRequestDTO.password()));
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    // Verifică credențialele și întoarce DTO-ul user-ului dacă parola e corectă.
    // Conține un fallback de migrare: dacă în DB există o parolă veche în clar (de la
    // versiunea anterioară a aplicației, înainte de a hash-ui la register), o acceptăm
    // o singură dată și o re-salvăm hash-uită — așa nu rămân conturile existente blocate.
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
        // Legacy: parolă în clar (egală cu cea trimisă) => o acceptăm și o migrăm la hash.
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

    // Banare/debanare a unui utilizator. Doar un MODERATOR poate face asta.
    // Ban-ul este "indefinit": rămâne activ până când un moderator îl dezactivează (banned=false).
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
                    // Notificăm utilizatorul DOAR la banare (email + WhatsApp, asincron).
                    if (banned) {
                        notificationService.notifyUserBanned(saved);
                    }
                    return saved;
                })
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Verifică dacă utilizatorul (după id) are rolul de MODERATOR.
    @Transactional(readOnly = true)
    public boolean isModerator(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> "MODERATOR".equals(role.getRoleName())))
                .orElse(false);
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

        // 1. Validare și setare Parolă (DOAR dacă s-a completat o parolă nouă)
        if (dto.password() != null && !dto.password().trim().isEmpty()) {
            if (isValidPassword(dto.password())) {
                existingUser.setPasswordHash(passwordEncoder.encode(dto.password()));
            } else {
                // Am actualizat și textul erorii la minim 6 caractere
                throw new IllegalArgumentException("Eșec validare: Parola trebuie să aibă minim 6 caractere, să conțină o literă mică, o literă mare, o cifră și un caracter special (@#$%^&+=!).");
            }
        }

        // 2. Validare și setare Email (DOAR dacă s-a trimis un email)
        if (dto.email() != null && !dto.email().trim().isEmpty()) {
            if (isValidEmail(dto.email())) {
                existingUser.setEmail(dto.email());
            } else {
                throw new IllegalArgumentException("Eșec validare: Adresa de email nu respectă un format valid (ex. nume@domeniu.com).");
            }
        }

        // 3. Validare și setare Username (DOAR dacă s-a trimis un username)
        if (dto.username() != null && !dto.username().trim().isEmpty()) {
            if (isValid(dto.username())) {
                existingUser.setUsername(dto.username());
            } else {
                throw new IllegalArgumentException("Eșec validare: Username-ul trebuie să aibă între 5 și 32 de caractere și să conțină doar caractere alfanumerice.");
            }
        }

        return existingUser;
    }
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
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,32}$";
        return password != null && password.matches(regex);
    }
}
