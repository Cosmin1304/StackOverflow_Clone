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
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    // ========================================================================================
    // METODA: registerUser
    // ========================================================================================
    // Scop: Înregistrează un utilizator nou în sistem.
    //
    // Ce trebuie să faci:
    // 1. Criptează parola din user.getPasswordHash() folosind
    // passwordEncoder.encode(...)
    // și setează rezultatul înapoi pe user cu user.setPasswordHash(...)
    // DE CE: Parola vine de la client ca text simplu. Trebuie hash-uită înainte de
    // salvare.
    //
    // 2. Setează scorul inițial al utilizatorului la BigDecimal.ZERO
    // DE CE: Un user nou nu are reputație, pornește de la 0.
    //
    // 3. Salvează user-ul în baza de date cu userRepository.save(user)
    //
    // 4. Convertește entitatea salvată într-un UserResponseDTO și returnează-l
    // DE CE: Nu vrem să returnăm entitatea JPA direct (ar expune parola hash-uită
    // și alte detalii interne).
    // Folosim UserResponseDTO.fromEntity(...) pentru conversie.
    //
    // @Transactional — asigură că toate operațiile din metodă se execută într-o
    // singură tranzacție DB.
    // Dacă ceva eșuează, totul se face rollback (se anulează).
    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        // TODO: Implementează conform pașilor de mai sus
        if (userRequestDTO == null)
            return null;
        User user = userMapper.toEntity(userRequestDTO);
        return userMapper.toResponse(userRepository.save(user));
    }

    // ========================================================================================
    // METODA: findByUsername
    // ========================================================================================
    // Scop: Caută un utilizator după username și returnează un
    // Optional<UserResponseDTO>.
    //
    // Ce trebuie să faci:
    // 1. Apelează userRepository.findByUsername(username) — returnează
    // Optional<User>
    // 2. Folosește .map(UserResponseDTO::fromEntity) pentru a converti User în
    // UserResponseDTO
    // DE CE: .map() se aplică doar dacă Optional-ul conține o valoare (user-ul
    // există).
    // Dacă nu există, Optional rămâne gol.
    //
    // @Transactional(readOnly = true) — spune Spring-ului că această metodă doar
    // citește date,
    // nu modifică nimic. Permite optimizări de performanță.
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userMapper::toResponse);
    }

    // ========================================================================================
    // METODA: findUserEntityByUsername
    // ========================================================================================
    // Scop: Caută un utilizator după username dar returnează entitatea User (nu
    // DTO).
    //
    // Ce trebuie să faci:
    // 1. Apelează userRepository.findByUsername(username) și returnează rezultatul
    // direct.
    //
    // DE CE returnăm User și nu UserResponseDTO aici?
    // Această metodă e folosită intern de alte service-uri/controllere care au
    // nevoie de
    // entitatea completă (ex: pentru a seta autorul pe un Topic sau Answer).
    // DTO-urile sunt pentru a expune date către exterior (API responses).
    @Transactional(readOnly = true)
    public Optional<User> findUserEntityByUsername(String username) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: getAllUsers
    // ========================================================================================
    // Scop: Returnează o listă cu toți utilizatorii din sistem, ca
    // UserResponseDTO-uri.
    //
    // Ce trebuie să faci:
    // 1. Creează o listă goală de User: List<User> users = new ArrayList<>()
    // 2. Apelează userRepository.findAll() — returnează un Iterable<User>
    // Folosește .forEach(users::add) pentru a adăuga fiecare user în lista creată.
    // DE CE nu direct List? — CrudRepository.findAll() returnează Iterable, nu
    // List.
    // 3. Convertește lista de useri în lista de DTO-uri:
    // users.stream().map(UserResponseDTO::fromEntity).collect(Collectors.toList())
    // stream() = creează un flux de date din listă
    // map() = transformă fiecare element (User -> UserResponseDTO)
    // collect() = adună rezultatele într-o nouă Listă
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        // TODO: Implementează conform pașilor de mai sus
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    // ========================================================================================
    // METODA: deleteUser
    // ========================================================================================
    // Scop: Șterge un utilizator, DAR doar dacă utilizatorul curent (autentificat)
    // este chiar el.
    //
    // Parametri:
    // - userId: ID-ul user-ului de șters
    // - currentUserId: ID-ul user-ului care face cererea (extras din sesiunea
    // autentificată)
    //
    // Ce trebuie să faci:
    // 1. Verifică dacă userId ESTE EGAL cu currentUserId
    // Dacă NU → aruncă RuntimeException cu mesajul "Eroare de permisiune: Puteți
    // șterge doar propriul cont!"
    // DE CE: Un utilizator nu ar trebui să poată șterge contul altuia.
    // 2. Dacă DA → apelează userRepository.deleteById(userId)
    public void deleteUser(Long userId, Long currentUserId) {
        if (Objects.equals(userId, currentUserId))
            throw new RuntimeException("You can't delete this user");
        userRepository.deleteById(userId);
    }

    // ========================================================================================
    // METODA: updateUser
    // ========================================================================================
    // Scop: Actualizează datele unui utilizator (username, parolă), doar dacă e
    // contul propriu.
    //
    // Parametri:
    // - id: ID-ul user-ului de actualizat
    // - userDetails: obiect User cu noile date (username, parolă)
    // - currentUserId: ID-ul user-ului autentificat
    //
    // Ce trebuie să faci:
    // 1. Verifică dacă id == currentUserId. Dacă NU → aruncă RuntimeException.
    // DE CE: Același principiu de securitate — doar tu poți edita propriul cont.
    //
    // 2. Caută user-ul în DB: userRepository.findById(id)
    // Dacă nu există → aruncă RuntimeException("User not found")
    // .orElseThrow() face exact asta: dacă Optional e gol, aruncă excepția.
    //
    // 3. Actualizează username-ul: user.setUsername(userDetails.getUsername())
    //
    // 4. Dacă parola nouă NU e null și NU e goală:
    // Criptează noua parolă cu passwordEncoder.encode(...) și setează-o pe user.
    // DE CE verificăm null/goală? — Dacă clientul nu trimite parolă nouă, nu vrem
    // să o ștergem.
    //
    // 5. Salvează user-ul actualizat: userRepository.save(user)
    // 6. Returnează UserResponseDTO.fromEntity(...)
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDetails, Long currentUserId) {
        if (!Objects.equals(id, currentUserId))
            throw new RuntimeException("Nu poți edita contul altui utilizator");
            
        Predicate<String> checkForUsername = UserService::isValid;
        Predicate<String> checkForEmail = UserService::isValidEmail;
        Predicate<String> checkForPassword = UserService::isValidPassword;
        
        Function<UserRequestDTO, UserResponseDTO> returnUpdatedUser = (current) -> {
            //luăm user-ul din baza de date nu facem new User() ca să nu pierdem scorul/id-ul!
            User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            
            // dacă trece regex-ul (care verifică și != null), modifică.
            // dacă pică regex-ul pur și simplu nu execută if-ul și trece la următorul
            if (checkForPassword.test(current.userPassword()))
                existingUser.setPasswordHash(passwordEncoder.encode(current.userPassword()));
                
            if (checkForEmail.test(current.userEmail()))
                existingUser.setEmail(current.userEmail());
                
            if (checkForUsername.test(current.userName()))
                existingUser.setUsername(current.userName());
                
            return userMapper.toResponse(userRepository.save(existingUser));
        };
        
        return returnUpdatedUser.apply(userDetails);
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
