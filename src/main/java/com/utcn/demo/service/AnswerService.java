package com.utcn.demo.service;

import com.utcn.demo.dto.Mappers.AnswerMapper;
import com.utcn.demo.dto.Requests.AnswerRequestDTO;
import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utcn.demo.dto.Responses.AnswerResponseDTO;

import java.util.List;
import java.util.Objects;

// AnswerService — conține logica de business pentru răspunsuri (Answer).
// Un Answer este un răspuns la un Topic (întrebare pe forum).
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final TopicRepository topicRepository;
    private final com.utcn.demo.repository.UserRepository userRepository;

    @Transactional
    public AnswerResponseDTO addAnswer(Long topicId, AnswerRequestDTO answerDTO, Long currentUserId) {
        return topicRepository.findById(topicId)
                .map(this::validateTopicIsUnsolved)
                .map(this::updateTopicStatusIfNeeded)
                .map(topic -> createAnswerEntity(answerDTO, topic, currentUserId))
                .map(answerRepository::save)
                .map(answerMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
    }

    private Topic validateTopicIsUnsolved(Topic topic) {
        if ("SOLVED".equals(topic.getStatus())) {
            throw new RuntimeException("This topic is already solved, no more answers can be added");
        }
        return topic;
    }

    private Topic updateTopicStatusIfNeeded(Topic topic) {
        if (topic.getAnswers().isEmpty() && "RECEIVED".equals(topic.getStatus())) {
            topic.setStatus("IN_PROGRESS");
            topicRepository.save(topic);
        }
        return topic;
    }

    private Answer createAnswerEntity(AnswerRequestDTO dto, Topic topic, Long currentUserId) {
        Answer answer = answerMapper.toEntity(dto);
        answer.setCreatedAt(java.time.LocalDateTime.now()); //this may be optional
        answer.setTopic(topic);

        userRepository.findById(currentUserId).ifPresent(answer::setAuthor);
        if (answer.getAuthor() == null) {
            throw new RuntimeException("Author not found");
        }
        return answer;
    }

    @Transactional(readOnly = true)
    public List<AnswerResponseDTO> getAnswersByTopic(Long topicId) {
        return topicRepository.findById(topicId)
                .map(topic -> new java.util.ArrayList<>(topic.getAnswers()))
                .orElseThrow(() -> new RuntimeException("Topic not found"))
                .stream()
                .map(answerMapper::toResponse)
                .sorted((a1, a2) -> {
                    int score1 = a1.votes().stream().mapToInt(v -> "UPVOTE".equals(v.voteType()) ? 1 : -1).sum();
                    int score2 = a2.votes().stream().mapToInt(v -> "UPVOTE".equals(v.voteType()) ? 1 : -1).sum();
                    return Integer.compare(score2, score1);
                })
                .toList();
    }

    @Transactional
    public AnswerResponseDTO updateAnswer(Long answerId, String newText, Long currentUserId) {
        return answerRepository.findById(answerId)
                .map(answer -> {
                    if (!Objects.equals(answer.getAuthor().getId(), currentUserId)) {
                        throw new RuntimeException("Only the author can edit this answer!");
                    }
                    answer.setTextContent(newText);
                    return answer;
                })
                .map(answerRepository::save)
                .map(answerMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
    }

    @Transactional
    public void deleteAnswer(Long answerId, Long currentUserId) {
        answerRepository.findById(answerId).ifPresentOrElse(
                answer -> {
                    if (!Objects.equals(answer.getAuthor().getId(), currentUserId)) {
                        throw new RuntimeException("Only the author can delete this answer!");
                    }
                    answerRepository.delete(answer);
                },
                () -> {
                    throw new RuntimeException("Answer not found");
                }
        );
    }
}
