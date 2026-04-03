package com.utcn.demo.controller;

import com.utcn.demo.entity.User;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails, Principal principal) {
        Long actualUserId = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Sesiune invalida")).getId();
        return userService.updateUser(id, userDetails, actualUserId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, Principal principal) {
        Long actualUserId = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Sesiune invalida")).getId();
        userService.deleteUser(id, actualUserId);
    }
}
