package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "answers")
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text_content", nullable = false, columnDefinition = "TEXT")
    private String textContent;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- RELAȚIILE ---

    // Relația M:1 cu Topic (Acest răspuns aparține de un singur Topic)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    // Relația M:1 cu User (Acest răspuns a fost scris de un singur autor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Relația 1:N cu Voturile Răspunsului
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnswerVote> votes = new HashSet<>();
}