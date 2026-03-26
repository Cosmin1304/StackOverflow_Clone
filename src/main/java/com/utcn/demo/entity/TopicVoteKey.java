package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

// 4.1 Clasa pentru Cheia Compusă
@Embeddable
@Getter
@Setter
public class TopicVoteKey implements Serializable {
    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "user_id")
    private Long userId;

    // equals() și hashCode() sunt obligatorii în Java pentru clasele @Embeddable!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicVoteKey that = (TopicVoteKey) o;
        return Objects.equals(topicId, that.topicId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicId, userId);
    }
}