package com.utcn.demo.service;

import com.utcn.demo.dto.Mappers.UserMapper;
import com.utcn.demo.dto.Requests.TopicRequestDTO;
import com.utcn.demo.dto.Requests.UserRequestDTO;
import com.utcn.demo.dto.Responses.UserResponseDTO;
import com.utcn.demo.entity.*;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TopicVoteRepository;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

// VoteService — conține logica de business pentru voturile pe topic-uri.
// Sistemul de vot permite utilizatorilor să dea UPVOTE sau DOWNVOTE pe topic-uri.
// Voturile afectează scorul (reputația) autorului topic-ului.
@Service
@RequiredArgsConstructor
public class VoteService {
    private final TopicVoteRepository topicVoteRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    // repository-uri adaugate pt raspunsuri
    private final com.utcn.demo.repository.AnswerRepository answerRepository;
    private final com.utcn.demo.repository.AnswerVoteRepository answerVoteRepository;

    @Transactional
    public void voteTopic(Integer topicId, Long userId, String voteTypeString) {
        Topic topic = topicRepository.findById(Long.valueOf(topicId))
                .orElseThrow(() -> new RuntimeException("Topic doesn't exist"));

        Optional<TopicVote> existingVoteOpt = topicVoteRepository.findByTopicIdAndUserId(Long.valueOf(topicId), userId);

        if (existingVoteOpt.isPresent()) {
            TopicVote existingVote = existingVoteOpt.get();
            if (existingVote.getVoteType().equalsIgnoreCase(voteTypeString)) {
                topicVoteRepository.delete(existingVote);
            } else {
                existingVote.setVoteType(voteTypeString.toUpperCase());
                topicVoteRepository.save(existingVote);
            }
            return;
        }

        processVote(userId, topic.getAuthor(),
                u -> topic.getVotes().stream().anyMatch(v -> v.getUser().getId().equals(u.getId())),
                voteTypeString, this::applyTopicScoreRules,
                u -> {
                    TopicVote tv = new TopicVote();
                    tv.setTopic(topic);
                    tv.setUser(u);
                    tv.setVoteType(voteTypeString.toUpperCase());
                    topicVoteRepository.save(tv);
                });
    }

    @Transactional
    public void voteAnswer(Long answerId, Long userId, String voteTypeString) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer doesn't exist"));

        Optional<AnswerVote> existingVoteOpt = answerVoteRepository.findByAnswerIdAndUserId(answerId, userId);

        if (existingVoteOpt.isPresent()) {
            AnswerVote existingVote = existingVoteOpt.get();
            if (existingVote.getVoteType().equalsIgnoreCase(voteTypeString)) {
                answerVoteRepository.delete(existingVote);
            } else {
                existingVote.setVoteType(voteTypeString.toUpperCase());
                answerVoteRepository.save(existingVote);
            }
            return;
        }

        processVote(userId, answer.getAuthor(),
                u -> answer.getVotes().stream().anyMatch(v -> v.getUser().getId().equals(u.getId())),
                voteTypeString, this::applyAnswerScoreRules,
                user -> strategyConsumerImplementation(user, answer, voteTypeString)
        );
    }

    private void strategyConsumerImplementation (User user, Answer answer , String voteTypeString) {
        AnswerVote av = new AnswerVote();
        av.setAnswer(answer);
        av.setUser(user);
        av.setVoteType(voteTypeString.toUpperCase());
        answerVoteRepository.save(av);
    }

    private void processVote(Long userId, User author, Predicate<User> alreadyVotedCheck,
                             String voteType, ScoreStrategy strategy, Consumer<User> voteSaver) {

        User voter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        if (author == null) throw new RuntimeException("Target has no author");
        if (author.getId().equals(voter.getId())) {
            throw new RuntimeException("You cannot vote your own content!");
        }

        if (alreadyVotedCheck.test(voter)) {
            throw new RuntimeException("User already voted on this content!");
        }

        strategy.apply(voter, author, voteType.toUpperCase());

        voteSaver.accept(voter);
        userRepository.save(author);
        userRepository.save(voter);
    }

    @FunctionalInterface
    private interface ScoreStrategy {
        void apply(User voter, User author, String voteType);
    }

    private void applyTopicScoreRules(User voter, User author, String voteType) {
        if ("UPVOTE".equals(voteType)) {
            author.setScore(author.getScore().add(new BigDecimal("2.5")));
        } else if ("DOWNVOTE".equals(voteType)) {
            author.setScore(author.getScore().subtract(new BigDecimal("1.5")));
        } else {
            throw new RuntimeException("Invalid vote type for topic");
        }
    }

    private void applyAnswerScoreRules(User voter, User author, String voteType) {
        if ("UPVOTE".equals(voteType)) {
            author.setScore(author.getScore().add(new BigDecimal("5.0")));
        } else if ("DOWNVOTE".equals(voteType)) {
            author.setScore(author.getScore().subtract(new BigDecimal("2.5")));
            voter.setScore(voter.getScore().subtract(new BigDecimal("1.5")));
        } else {
            throw new RuntimeException("Invalid vote type for answer");
        }
    }
}
