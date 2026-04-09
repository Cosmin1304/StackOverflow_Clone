package com.utcn.demo.controller;

import com.utcn.demo.entity.User;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import com.utcn.demo.dto.UserDTO;
import java.util.stream.Collectors;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody User userDetails, Principal principal) {
        Long actualUserId = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Sesiune invalida")).id();
        return userService.updateUser(id, userDetails, actualUserId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, Principal principal) {
        Long actualUserId = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Sesiune invalida")).id();
        userService.deleteUser(id, actualUserId);
    }
}
