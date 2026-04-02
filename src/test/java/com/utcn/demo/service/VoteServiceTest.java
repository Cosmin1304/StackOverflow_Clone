package com.utcn.demo.service;

import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.TopicVote;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TopicVoteRepository;
import com.utcn.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private TopicVoteRepository topicVoteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VoteService voteService;

    private User author;
    private User voter;
    private Topic topic;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);
        author.setScore(BigDecimal.ZERO);

        voter = new User();
        voter.setId(2L);

        topic = new Topic();
        topic.setId(1L);
        topic.setAuthor(author);
    }

    @Test
    void voteTopic_ShouldThrowException_WhenVotingOnOwnTopic() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            voteService.voteTopic(topic, author, "UPVOTE");
        });

        assertEquals("You cannot vote on your own topic", exception.getMessage());
        verify(topicVoteRepository, never()).save(any());
    }

    @Test
    void voteTopic_ShouldIncreaseScore_OnUpvote() {
        when(topicVoteRepository.save(any(TopicVote.class))).thenReturn(new TopicVote());
        when(userRepository.save(author)).thenReturn(author);

        voteService.voteTopic(topic, voter, "UPVOTE");

        assertEquals(new BigDecimal("2.5"), author.getScore());
        verify(topicVoteRepository).save(any(TopicVote.class));
        verify(userRepository).save(author);
    }

    @Test
    void voteTopic_ShouldDecreaseScore_OnDownvote() {
        when(topicVoteRepository.save(any(TopicVote.class))).thenReturn(new TopicVote());
        when(userRepository.save(author)).thenReturn(author);

        voteService.voteTopic(topic, voter, "DOWNVOTE");

        assertEquals(new BigDecimal("-1.5"), author.getScore());
        verify(topicVoteRepository).save(any(TopicVote.class));
        verify(userRepository).save(author);
    }
}
