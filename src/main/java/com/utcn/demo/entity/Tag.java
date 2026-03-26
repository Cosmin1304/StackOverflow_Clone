package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // Relația inversă M:N cu Topic (mappedBy arată spre proprietatea 'tags' din clasa Topic)
    @ManyToMany(mappedBy = "tags")
    private Set<Topic> topics = new HashSet<>();
}