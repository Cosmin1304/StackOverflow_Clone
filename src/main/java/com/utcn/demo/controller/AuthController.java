package com.utcn.demo.controller;

import com.utcn.demo.dto.Requests.LoginRequestDTO;
import com.utcn.demo.dto.Responses.UserResponseDTO;
import com.utcn.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO credentials) {

        System.out.println("Incercare de logare in baza de date pentru: " + credentials.username());

        return userService.findByUsername(credentials.username())
                .map(user -> ResponseEntity.ok(user)) //se poate folosi si UserMapper pentru a returna un DTO in loc de User
                .orElse(ResponseEntity.status(401).build());
    }
}

