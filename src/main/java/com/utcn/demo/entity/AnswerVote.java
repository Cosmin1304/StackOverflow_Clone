package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "answer_votes")
@Getter
@Setter
public class AnswerVote {

    @EmbeddedId
    private AnswerVoteKey id = new AnswerVoteKey();

    @ManyToOne
    @MapsId("answerId")
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "vote_value", nullable = false, length = 20)
    private String voteType;

}