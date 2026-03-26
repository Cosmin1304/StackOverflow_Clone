package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "topics")
@Getter
@Setter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "text_content", nullable = false, columnDefinition = "TEXT")
    private String textContent;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(nullable = false)
    private String status = "RECEIVED"; // RECEIVED, IN_PROGRESS, SOLVED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- RELAȚIILE ---

    // Relația M:1 cu User (Mai multe topic-uri cu un singur autor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Relația 1:N cu Answers (Un topic are mai multe răspunsuri)
    // CascadeType.ALL și orphanRemoval înseamnă că dacă ștergi topicul, se șterg și răspunsurile
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    // Relația M:N cu Tags (Un topic are mai multe tag-uri)
    @ManyToMany
    @JoinTable(
            name = "topic_tags",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Relația 1:N cu Voturile Topic-ului
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TopicVote> votes = new HashSet<>();
}