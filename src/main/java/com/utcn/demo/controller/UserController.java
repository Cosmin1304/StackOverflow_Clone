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
@lombok.RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public UserResponseDTO registerUser(@RequestBody com.utcn.demo.dto.Requests.UserRequestDTO user) {
        return userService.registerUser(user);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody com.utcn.demo.dto.Requests.UserRequestDTO userDetails, Principal principal) {
        return userService.findByUsername(principal.getName())
                .map(user -> userService.updateUser(id, userDetails, user.id()))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, Principal principal) {
        userService.findByUsername(principal.getName())
                .ifPresentOrElse(
                        user -> userService.deleteUser(id, user.id()),
                        () -> { throw new RuntimeException("Sesiune invalida"); }
                );
    }
}
