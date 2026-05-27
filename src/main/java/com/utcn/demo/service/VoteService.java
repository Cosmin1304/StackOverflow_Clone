package com.utcn.demo.service;

import com.utcn.demo.entity.*;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.AnswerVoteRepository;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TopicVoteRepository;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

// VoteService — logica de business pentru voturi (pe topic-uri și pe răspunsuri).
// Voturile afectează REPUTAȚIA (scorul) utilizatorilor:
//   - Topic  UPVOTE   : autorul +2.5
//   - Topic  DOWNVOTE : autorul -1.5
//   - Answer UPVOTE   : autorul +5
//   - Answer DOWNVOTE : autorul -2.5  ȘI  votantul -1.5 (penalizare pentru down-vote pe răspuns)
//
// Reputația pleacă de la 0, poate fi negativă și folosește zecimale (BigDecimal).
//
// CONSTRÂNGERE IMPORTANTĂ: la schimbarea sau anularea unui vot, întâi ANULĂM (revert)
// punctele vechi și abia apoi le aplicăm pe cele noi, ca scorul să rămână mereu corect.
@Service
@RequiredArgsConstructor
public class VoteService {
    private final TopicVoteRepository topicVoteRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final AnswerVoteRepository answerVoteRepository;

    // Valorile de reputație (pozitive); semnul corect se aplică mai jos.
    private static final BigDecimal QUESTION_UPVOTE = new BigDecimal("2.5");
    private static final BigDecimal QUESTION_DOWNVOTE = new BigDecimal("1.5");
    private static final BigDecimal ANSWER_UPVOTE = new BigDecimal("5.0");
    private static final BigDecimal ANSWER_DOWNVOTE = new BigDecimal("2.5");
    private static final BigDecimal ANSWER_DOWNVOTE_VOTER_PENALTY = new BigDecimal("1.5");

    private static final String UPVOTE = "UPVOTE";
    private static final String DOWNVOTE = "DOWNVOTE";

    @Transactional
    public void voteTopic(Integer topicId, Long userId, String voteTypeString) {
        Topic topic = topicRepository.findById(Long.valueOf(topicId))
                .orElseThrow(() -> new RuntimeException("Topic doesn't exist"));

        User author = topic.getAuthor();
        User voter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        String newType = validateVote(author, voter, voteTypeString);

        Optional<TopicVote> existingVoteOpt = topicVoteRepository.findByTopicIdAndUserId(Long.valueOf(topicId), userId);

        if (existingVoteOpt.isEmpty()) {
            // 1) Vot nou: aplicăm punctele.
            applyTopicScore(author, newType, false);
            TopicVote tv = new TopicVote();
            tv.setTopic(topic);
            tv.setUser(voter);
            tv.setVoteType(newType);
            topicVoteRepository.save(tv);
        } else {
            TopicVote existingVote = existingVoteOpt.get();
            String oldType = existingVote.getVoteType().toUpperCase();

            // Întâi ANULĂM efectul votului vechi, indiferent ce urmează.
            applyTopicScore(author, oldType, true);

            if (oldType.equals(newType)) {
                // 2) Același vot reapăsat => anulare (toggle off): am revertat deja, ștergem votul.
                topicVoteRepository.delete(existingVote);
            } else {
                // 3) Schimbare de vot (UPVOTE <-> DOWNVOTE): aplicăm punctele noului tip.
                applyTopicScore(author, newType, false);
                existingVote.setVoteType(newType);
                topicVoteRepository.save(existingVote);
            }
        }

        userRepository.save(author);
    }

    @Transactional
    public void voteAnswer(Long answerId, Long userId, String voteTypeString) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer doesn't exist"));

        User author = answer.getAuthor();
        User voter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        String newType = validateVote(author, voter, voteTypeString);

        Optional<AnswerVote> existingVoteOpt = answerVoteRepository.findByAnswerIdAndUserId(answerId, userId);

        if (existingVoteOpt.isEmpty()) {
            // 1) Vot nou.
            applyAnswerScore(voter, author, newType, false);
            AnswerVote av = new AnswerVote();
            av.setAnswer(answer);
            av.setUser(voter);
            av.setVoteType(newType);
            answerVoteRepository.save(av);
        } else {
            AnswerVote existingVote = existingVoteOpt.get();
            String oldType = existingVote.getVoteType().toUpperCase();

            // Întâi ANULĂM efectul votului vechi (inclusiv penalizarea votantului).
            applyAnswerScore(voter, author, oldType, true);

            if (oldType.equals(newType)) {
                // 2) Anulare (toggle off).
                answerVoteRepository.delete(existingVote);
            } else {
                // 3) Schimbare de vot.
                applyAnswerScore(voter, author, newType, false);
                existingVote.setVoteType(newType);
                answerVoteRepository.save(existingVote);
            }
        }

        userRepository.save(author);
        userRepository.save(voter);
    }

    // Validează votul și întoarce tipul normalizat (UPPERCASE).
    private String validateVote(User author, User voter, String voteTypeString) {
        if (author == null) throw new RuntimeException("Target has no author");
        if (author.getId().equals(voter.getId())) {
            throw new RuntimeException("You cannot vote your own content!");
        }
        if (voteTypeString == null) throw new RuntimeException("Vote type is required");
        String normalized = voteTypeString.toUpperCase();
        if (!UPVOTE.equals(normalized) && !DOWNVOTE.equals(normalized)) {
            throw new RuntimeException("Invalid vote type");
        }
        return normalized;
    }

    // Aplică (revert=false) sau anulează (revert=true) punctele unui vot pe TOPIC.
    private void applyTopicScore(User author, String voteType, boolean revert) {
        if (UPVOTE.equals(voteType)) {
            addScore(author, QUESTION_UPVOTE, revert);
        } else if (DOWNVOTE.equals(voteType)) {
            addScore(author, QUESTION_DOWNVOTE.negate(), revert);
        } else {
            throw new RuntimeException("Invalid vote type for topic");
        }
    }

    // Aplică (revert=false) sau anulează (revert=true) punctele unui vot pe ANSWER.
    // La DOWNVOTE, autorul pierde puncte ȘI votantul primește o penalizare.
    private void applyAnswerScore(User voter, User author, String voteType, boolean revert) {
        if (UPVOTE.equals(voteType)) {
            addScore(author, ANSWER_UPVOTE, revert);
        } else if (DOWNVOTE.equals(voteType)) {
            addScore(author, ANSWER_DOWNVOTE.negate(), revert);
            addScore(voter, ANSWER_DOWNVOTE_VOTER_PENALTY.negate(), revert);
        } else {
            throw new RuntimeException("Invalid vote type for answer");
        }
    }

    // Adună delta la scor; dacă revert=true, adună inversul (anulează efectul).
    private static void addScore(User user, BigDecimal delta, boolean revert) {
        BigDecimal current = user.getScore() != null ? user.getScore() : BigDecimal.ZERO;
        user.setScore(current.add(revert ? delta.negate() : delta));
    }
}
