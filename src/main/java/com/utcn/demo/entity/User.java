package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Folosim BigDecimal pentru precizie la numere cu virgulă (ex: +2.5, -1.5)
    @Column(precision = 10, scale = 2)
    private BigDecimal score = BigDecimal.ZERO;

    @Column(name = "is_banned")
    private Boolean isBanned = false;

    // --- RELAȚIILE ---

    // Relația M:N cu Role (Un user poate fi USER și/sau MODERATOR)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    //pentru functionalitatea user service
    @Transient  //metodele se set si get vor fi generate, dar nu va avea o coloana in tabel
    private String password;

}