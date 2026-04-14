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

import static org.mockito.ArgumentMatchers.any;

// ========================================================================================
// TESTE UNITARE PENTRU UserService
// ========================================================================================
//
// Testele unitare verifică logica de business IZOLAT, fără a porni aplicația sau baza de date.
//
// @ExtendWith(MockitoExtension.class) — activează Mockito (framework de mocking) pentru teste.
// Mockito permite crearea de obiecte "false" (mock-uri) care simulează comportamentul
// repository-urilor și altor dependențe, fără a accesa baza de date reală.
//
// DE CE testăm cu mock-uri?
// - Testele sunt rapide (nu pornesc Spring, nu accesează DB)
// - Testăm DOAR logica service-ului, nu a repository-ului sau a bazei de date
// - Putem simula orice scenariu (user exists, user not found, etc.)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // @Mock — creează un obiect "fals" care simulează UserRepository.
    // Nu accesează baza de date reală. Noi îi spunem ce să returneze cu when(...).thenReturn(...)
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // @InjectMocks — creează o instanță reală de UserService și injectează mock-urile de mai sus.
    // Deci userService.userRepository va fi mock-ul nostru, nu un repository real.
    @InjectMocks
    private UserService userService;

    private User user;

    // @BeforeEach — se execută ÎNAINTE de fiecare test (@Test).
    // Scop: pregătește datele de test (un User cu date predefinite).
    // DE CE: Fiecare test trebuie să pornească cu date curate și predictibile.
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPasswordHash("rawPassword");
    }

    // ========================================================================================
    // TEST: registerUser_ShouldHashPasswordAndSave
    // ========================================================================================
    // Ce testăm: Că metoda registerUser() hash-uiește parola și salvează user-ul corect.
    //
    // Ce trebuie să faci:
    // 1. ARANJEAZĂ (Arrange) — configurează mock-urile:
    //    when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");
    //    → Când service-ul apelează encode, mock-ul returnează "hashedPassword"
    //    when(userRepository.save(any(User.class))).thenReturn(user);
    //    → Când se salvează orice User, returnează user-ul nostru de test
    //
    // 2. ACȚIONEAZĂ (Act) — apelează metoda de testat:
    //    UserResponseDTO savedUserDTO = userService.registerUser(user);
    //
    // 3. VERIFICĂ (Assert):
    //    assertEquals("hashedPassword", user.getPasswordHash());
    //    → Parola a fost hash-uită?
    //    assertEquals(BigDecimal.ZERO, user.getScore());
    //    → Scorul a fost setat la 0?
    //    verify(userRepository).save(user);
    //    → S-a apelat save() pe repository? (verify = confirmă că metoda a fost chemată)
    @Test
    void registerUser_ShouldHashPasswordAndSave() {
        // TODO: Implementează testul conform pașilor Arrange/Act/Assert de mai sus
    }

    // ========================================================================================
    // TEST: findByUsername_ShouldReturnUser_WhenExists
    // ========================================================================================
    // Ce testăm: Că findByUsername returnează user-ul când acesta există.
    //
    // Ce trebuie să faci:
    // 1. Arrange: when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
    // 2. Act: Optional<UserResponseDTO> result = userService.findByUsername("testuser");
    // 3. Assert:
    //    assertTrue(result.isPresent()); → Optional-ul conține o valoare?
    //    assertEquals("testuser", result.get().username()); → Username-ul e corect?
    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: getAllUsers_ShouldReturnList
    // ========================================================================================
    // Ce testăm: Că getAllUsers returnează o listă nevidă.
    //
    // Ce trebuie să faci:
    // 1. Arrange: when(userRepository.findAll()).thenReturn(List.of(user));
    // 2. Act: List<UserResponseDTO> users = userService.getAllUsers();
    // 3. Assert: assertFalse(users.isEmpty()); → Lista nu e goală?
    @Test
    void getAllUsers_ShouldReturnList() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: deleteUser_ShouldCallRepositoryDelete_WhenCurrentUserIsOwner
    // ========================================================================================
    // Ce testăm: Că delete funcționează când user-ul curent e chiar el (id-uri egale).
    //
    // Ce trebuie să faci:
    // 1. Arrange: doNothing().when(userRepository).deleteById(1L);
    //    doNothing() e pentru metode void — spune "nu face nimic când e apelată"
    // 2. Act: userService.deleteUser(1L, 1L); → ambele ID-uri sunt 1 (e propriul cont)
    // 3. Assert: verify(userRepository).deleteById(1L); → S-a apelat deleteById?
    @Test
    void deleteUser_ShouldCallRepositoryDelete_WhenCurrentUserIsOwner() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: deleteUser_ShouldThrowException_WhenCurrentUserIsNotOwner
    // ========================================================================================
    // Ce testăm: Că delete aruncă excepție când cineva încearcă să șteargă contul altuia.
    //
    // Ce trebuie să faci:
    // 1. Act + Assert:
    //    assertThrows(RuntimeException.class, () -> userService.deleteUser(1L, 2L));
    //    → ID-uri diferite: user 2 încearcă să șteargă user 1 → excepție
    //    verify(userRepository, never()).deleteById(anyLong());
    //    → Confirmă că deleteById NU a fost apelat (never() = niciodată)
    @Test
    void deleteUser_ShouldThrowException_WhenCurrentUserIsNotOwner() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: updateUser_ShouldUpdateDetailsAndSave_WhenCurrentUserIsOwner
    // ========================================================================================
    // Ce testăm: Că update funcționează corect când user-ul e proprietar.
    //
    // Ce trebuie să faci:
    // 1. Arrange:
    //    - Creează un User "updatedDetails" cu username="newuser", passwordHash="newRawPassword"
    //    - when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    //    - when(passwordEncoder.encode("newRawPassword")).thenReturn("newHashedPassword");
    //    - when(userRepository.save(any(User.class))).thenReturn(user);
    //
    // 2. Act: UserResponseDTO result = userService.updateUser(1L, updatedDetails, 1L);
    //
    // 3. Assert:
    //    assertEquals("newuser", result.username());
    //    assertEquals("newHashedPassword", user.getPasswordHash());
    //    verify(userRepository).save(user);
    @Test
    void updateUser_ShouldUpdateDetailsAndSave_WhenCurrentUserIsOwner() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: updateUser_ShouldThrowException_WhenCurrentUserIsNotOwner
    // ========================================================================================
    // Ce testăm: Că update aruncă excepție când ID-urile nu se potrivesc.
    //
    // Ce trebuie să faci:
    // assertThrows(RuntimeException.class, () -> userService.updateUser(1L, new User(), 2L));
    @Test
    void updateUser_ShouldThrowException_WhenCurrentUserIsNotOwner() {
        // TODO: Implementează
    }
}
