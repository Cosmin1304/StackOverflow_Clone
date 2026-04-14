package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.UserResponseDTO;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;

// @RestController — combină @Controller + @ResponseBody.
// Spune Spring-ului că aceasta e o clasă care primește request-uri HTTP și returnează JSON direct
// (nu view-uri HTML). Fiecare metodă returnează un obiect care se serializează automat în JSON.
//
// @RequestMapping("/api/users") — toate endpoint-urile din această clasă vor începe cu /api/users.
// Ex: GET /api/users, POST /api/users/register, DELETE /api/users/5
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Service-ul care conține logica de business. Controller-ul NU ar trebui să conțină logică —
    // el doar primește request-ul HTTP, extrage datele, apelează service-ul, și returnează rezultatul.
    @Autowired
    private UserService userService;

    // ========================================================================================
    // ENDPOINT: GET /api/users
    // ========================================================================================
    // Scop: Returnează lista tuturor utilizatorilor.
    //
    // @GetMapping — mapează request-urile HTTP GET pe această metodă.
    // Când cineva face GET /api/users → se apelează această metodă.
    //
    // Ce trebuie să faci:
    // 1. Apelează userService.getAllUsers() și returnează rezultatul.
    //    Rezultatul (List<UserResponseDTO>) se serializează automat în JSON de Spring.
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        // TODO: Implementează — apelează service-ul și returnează lista
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: GET /api/users/{username}
    // ========================================================================================
    // Scop: Caută un utilizator după username.
    //
    // @PathVariable — extrage valoarea {username} din URL.
    //   Ex: GET /api/users/john → username = "john"
    //
    // ResponseEntity — wrapper Spring care permite controlul codului HTTP de răspuns.
    //   ResponseEntity.ok(body) → HTTP 200 + body
    //   ResponseEntity.notFound().build() → HTTP 404
    //
    // Ce trebuie să faci:
    // 1. Apelează userService.findByUsername(username) — returnează Optional<UserResponseDTO>
    // 2. Dacă user-ul există → .map(ResponseEntity::ok) → HTTP 200 cu user-ul
    //    Dacă NU → .orElse(ResponseEntity.notFound().build()) → HTTP 404
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: POST /api/users/register
    // ========================================================================================
    // Scop: Înregistrează un utilizator nou.
    //
    // @PostMapping — mapează request-uri HTTP POST.
    // @RequestBody — Spring deserializează automat JSON-ul din body-ul request-ului
    //   într-un obiect User. Ex: {"username": "john", "passwordHash": "parola123"}
    //
    // Ce trebuie să faci:
    // 1. Apelează userService.registerUser(user) și returnează rezultatul.
    @PostMapping("/register")
    public UserResponseDTO registerUser(@RequestBody User user) {
        // TODO: Implementează — apelează service-ul și returnează DTO-ul
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: PUT /api/users/{id}
    // ========================================================================================
    // Scop: Actualizează datele unui utilizator (doar propriul cont).
    //
    // Principal — obiect Spring Security care conține informații despre user-ul autentificat.
    //   principal.getName() → returnează username-ul user-ului logat.
    //   DE CE Principal? — Avem nevoie de ID-ul user-ului curent pentru verificarea permisiunii.
    //   Dar Principal ne dă doar username-ul → trebuie să căutăm user-ul după username ca să luăm ID-ul.
    //
    // Ce trebuie să faci:
    // 1. Extrage ID-ul user-ului curent:
    //    a) Apelează userService.findByUsername(principal.getName())
    //    b) .orElseThrow(() -> new RuntimeException("Sesiune invalida"))
    //    c) .id() → extrage ID-ul din UserResponseDTO (record)
    //
    // 2. Apelează userService.updateUser(id, userDetails, actualUserId)
    // 3. Returnează rezultatul.
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody User userDetails, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: DELETE /api/users/{id}
    // ========================================================================================
    // Scop: Șterge un utilizator (doar propriul cont).
    //
    // Ce trebuie să faci:
    // 1. Extrage ID-ul user-ului curent (la fel ca la update — prin Principal + findByUsername)
    // 2. Apelează userService.deleteUser(id, actualUserId)
    //    void = nu returnează nimic. Spring va trimite HTTP 200 fără body.
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }
}
