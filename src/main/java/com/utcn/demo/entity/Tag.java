package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tags")
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    // Relația M:N a fost actualizată pentru a pointa către TOPIC, nu Question
    @ManyToMany(mappedBy = "tags")
    private Set<Topic> topics = new HashSet<>();
}