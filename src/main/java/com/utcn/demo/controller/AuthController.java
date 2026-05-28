package com.utcn.demo.controller;

import com.utcn.demo.config.JwtUtil;
import com.utcn.demo.dto.Requests.LoginRequestDTO;
import com.utcn.demo.dto.Responses.AuthResponseDTO;
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
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO credentials) {

        System.out.println("Incercare de logare in baza de date pentru: " + credentials.username());

        return userService.authenticate(credentials.username(), credentials.password())
                .map(user -> {
                    if (Boolean.TRUE.equals(user.isBanned())) {
                        return ResponseEntity.status(403).<AuthResponseDTO>build();
                    }

                    String token = jwtUtil.generateToken(user.username());
                    return ResponseEntity.ok(new AuthResponseDTO(token, user));
                })
                .orElse(ResponseEntity.status(401).build());
    }
}

