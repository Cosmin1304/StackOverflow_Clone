package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.UserResponseDTO;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:4200")
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

    @PutMapping("/{id}/ban-status")
    public UserResponseDTO setBanStatus(@PathVariable Long id, @RequestParam boolean banned, Principal principal) {
        if (principal == null) throw new RuntimeException("Sesiune invalida");

        return userService.findByUsername(principal.getName())
                .map(requester -> userService.setBanStatus(id, banned, requester.id()))
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
