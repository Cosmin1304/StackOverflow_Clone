package com.utcn.demo;

import com.utcn.demo.entity.User;
import com.utcn.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserService userService) {
        return args -> {

            Optional<User> existingUser = userService.findByUsername("admin");

            if (existingUser.isEmpty()) {
                User testUser = new User();
                testUser.setUsername("admin");

                testUser.setPasswordHash("parola123");
                testUser.setEmail("admin@test.com");

                userService.registerUser(testUser);
                System.out.println(">>> Utilizator de test creat: admin / parola123");
            } else {
                System.out.println(">>> Utilizatorul admin există deja în baza de date.");
            }
        };
    }
}