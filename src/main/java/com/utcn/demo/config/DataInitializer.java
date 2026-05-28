package com.utcn.demo.config;

import com.utcn.demo.entity.Role;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.RoleRepository;
import com.utcn.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        userRepository.findByUsername("moderator").ifPresent(this::ensureModeratorRoleAndDedup);
    }

    private void ensureModeratorRoleAndDedup(User user) {
        Role moderatorRole = roleRepository.findByRoleName("MODERATOR")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("MODERATOR");
                    return roleRepository.save(newRole);
                });

        // Compara prin id, nu prin referința de obiect (Role nu are equals/hashCode custom),
        // altfel `contains` returna fals și rolul era adăugat din nou la fiecare pornire.
        List<Role> currentRoles = new ArrayList<>(user.getRoles());
        Set<Long> seenIds = new HashSet<>();
        List<Role> deduped = new ArrayList<>();
        for (Role r : currentRoles) {
            if (r == null || r.getId() == null) continue;
            if (seenIds.add(r.getId())) {
                deduped.add(r);
            }
        }

        boolean hasModerator = deduped.stream()
                .anyMatch(r -> "MODERATOR".equals(r.getRoleName()));
        if (!hasModerator) {
            deduped.add(moderatorRole);
            System.out.println(">>> SUCCESS: Utilizatorul a primit rolul de MODERATOR!");
        }

        // Rescriem lista doar dacă s-a schimbat ceva (curățire duplicate sau adăugare).
        if (deduped.size() != currentRoles.size() || !hasModerator) {
            user.getRoles().clear();
            user.getRoles().addAll(deduped);
            userRepository.save(user);
            System.out.println(">>> Roluri utilizator 'moderator' actualizate: " + deduped.size() + " (de-duplicate).");
        }
    }
}
