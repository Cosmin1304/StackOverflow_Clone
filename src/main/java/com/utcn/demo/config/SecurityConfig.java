package com.utcn.demo.config;

import com.utcn.demo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Dezactivăm CSRF pentru a putea testa API-ul ușor
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/api/users/register").permitAll() // H2 and Registration are
                                                                                              // open
                        .anyRequest().authenticated() // Any other request requires login
                )
                .formLogin(withDefaults()) // Activează formularul de login standard
                .httpBasic(withDefaults()); // Activează și Basic Auth (util pentru Postman)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Această metodă „conectează” Spring Security la baza ta de date
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPasswordHash()) // Folosește parola hash-uită din DB
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User nu a fost găsit: " + username));
    }
}