package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class AnswerVoteKey implements Serializable {
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerVoteKey that = (AnswerVoteKey) o;
        return Objects.equals(answerId, that.answerId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerId, userId);
    }
}