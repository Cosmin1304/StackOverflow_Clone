package com.utcn.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    public enum QuestionStatus {
        RECEIVED, IN_PROGRESS, SOLVED
    }

    @Id
    @Column(name="question_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne()
    @JoinColumn(name="author", nullable = false)
    private User user;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="text", nullable = false)
    private String text;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name="picture", nullable = false)
    private String picture;

    @Column(name="creation_date_time", nullable = false)
    private java.time.LocalDateTime creationDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private QuestionStatus status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "question_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("likeCount DESC")
    private List<Answer> answers;

}
