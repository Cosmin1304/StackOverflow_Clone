package com.utcn.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// SecurityConfig TEMPORAR — permite toate request-urile fără autentificare.
// Când vei face microserviciul de autentificare, vei înlocui această configurație
// cu una care validează token-uri JWT (sau alt mecanism) de la serviciul de auth.
@Configuration
public class SecurityConfig {

    // Permite TOATE request-urile fără autentificare.
    // CSRF dezactivat — necesar pentru REST API-uri (altfel POST/PUT/DELETE sunt blocate).
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }

    // PasswordEncoder — folosit pentru hash-uirea parolelor la register/update.
    // BCrypt rămâne necesar chiar și fără autentificare activă,
    // deoarece UserService îl folosește la salvarea user-ilor.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}