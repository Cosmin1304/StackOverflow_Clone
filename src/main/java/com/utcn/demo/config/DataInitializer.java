package com.utcn.demo.config;

import com.utcn.demo.entity.Role;
import com.utcn.demo.repository.RoleRepository;
import com.utcn.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.findByUsername("moderator").ifPresent(user -> {
            Role moderatorRole = roleRepository.findByRoleName("MODERATOR")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName("MODERATOR");
                        return roleRepository.save(newRole);
                    });

            if (!user.getRoles().contains(moderatorRole)) {
                user.getRoles().add(moderatorRole);
                userRepository.save(user);
                System.out.println(">>> SUCCESS: Utilizatorul a primit rolul de MODERATOR!");
            }
        });
    }
}