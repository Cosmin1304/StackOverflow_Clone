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

        return userService.findByUsername(credentials.username())
                .map(user -> {
                    // 2. Generăm token-ul JWT pe baza username-ului
                    String token = jwtUtil.generateToken(user.username());

                    // 3. Returnăm noul AuthResponseDTO care conține token-ul și datele user-ului
                    return ResponseEntity.ok(new AuthResponseDTO(token, user));
                })
                .orElse(ResponseEntity.status(401).build());
    }
}

