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

// Teste unitare pentru VoteService.
// Testăm: auto-vot interzis, upvote crește scorul, downvote scade scorul.
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
        author.setScore(BigDecimal.ZERO);  // Scor inițial = 0

        voter = new User();
        voter.setId(2L);  // Voter e un alt user (ID diferit de author)

        topic = new Topic();
        topic.setId(1L);
        topic.setAuthor(author);  // Topic-ul aparține lui author
    }

    // ========================================================================================
    // TEST: voteTopic_ShouldThrowException_WhenVotingOnOwnTopic
    // ========================================================================================
    // Ce testăm: Că un utilizator NU poate vota pe propriul topic.
    //
    // Ce trebuie să faci:
    // 1. Act + Assert:
    //    assertThrows(RuntimeException.class, () -> voteService.voteTopic(topic, author, "UPVOTE"));
    //    → author votează pe topic-ul propriu → excepție
    //    assertEquals("You cannot vote on your own topic", exception.getMessage());
    //    verify(topicVoteRepository, never()).save(any()); → Votul NU a fost salvat
    @Test
    void voteTopic_ShouldThrowException_WhenVotingOnOwnTopic() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: voteTopic_ShouldIncreaseScore_OnUpvote
    // ========================================================================================
    // Ce testăm: Că un UPVOTE crește scorul autorului cu 2.5.
    //
    // Ce trebuie să faci:
    // 1. Arrange:
    //    when(topicVoteRepository.save(any(TopicVote.class))).thenReturn(new TopicVote());
    //    when(userRepository.save(author)).thenReturn(author);
    //
    // 2. Act: voteService.voteTopic(topic, voter, "UPVOTE");
    //    voter (ID=2) votează pe topic-ul lui author (ID=1) → e permis
    //
    // 3. Assert:
    //    assertEquals(new BigDecimal("2.5"), author.getScore()); → Scorul a crescut de la 0 la 2.5?
    //    verify(topicVoteRepository).save(any(TopicVote.class)); → Votul a fost salvat?
    //    verify(userRepository).save(author); → Autorul (cu scor actualizat) a fost salvat?
    @Test
    void voteTopic_ShouldIncreaseScore_OnUpvote() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: voteTopic_ShouldDecreaseScore_OnDownvote
    // ========================================================================================
    // Ce testăm: Că un DOWNVOTE scade scorul autorului cu 1.5.
    //
    // Ce trebuie să faci: La fel ca testul de upvote, dar cu "DOWNVOTE".
    // Assert: assertEquals(new BigDecimal("-1.5"), author.getScore()); → 0 - 1.5 = -1.5
    @Test
    void voteTopic_ShouldDecreaseScore_OnDownvote() {
        // TODO: Implementează
    }
}
