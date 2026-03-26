package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "topic_votes")
@Getter
@Setter
public class TopicVote {

    @EmbeddedId
    private TopicVoteKey id = new TopicVoteKey();

    @ManyToOne
    @MapsId("topicId") // Leagă acest obiect de ID-ul din TopicVoteKey
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @MapsId("userId") // Leagă acest obiect de ID-ul din TopicVoteKey
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "vote_value", nullable = false, length = 20)
    private String voteType; // UPVOTE, DOWNVOTE, LIKE, DISLIKE
}