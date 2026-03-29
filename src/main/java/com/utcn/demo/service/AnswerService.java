package com.utcn.demo.service;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Transactional
    public Answer addAnswer(Long topicId, Answer answer) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        if ("SOLVED".equals(topic.getStatus())) {
            throw new RuntimeException("This topic is already solved, no more answers can be added");
        }

        answer.setCreatedAt(LocalDateTime.now());
        answer.setTopic(topic);

        if (topic.getAnswers().isEmpty() && "RECEIVED".equals(topic.getStatus())) {
            topic.setStatus("IN_PROGRESS");
            topicRepository.save(topic);
        }

        return answerRepository.save(answer);
    }

    public List<Answer> getAnswersByTopic(Long topicId) {
        return answerRepository.findByTopic_Id(topicId);
    }

    public Answer updateAnswer(Long answerId, String newTextContent, Long currentUserId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        if (!answer.getAuthor().getId().equals(currentUserId)) {
            throw new RuntimeException("Only the author can edit this answer!");
        }
        answer.setTextContent(newTextContent);
        return answerRepository.save(answer);
    }

    public void deleteAnswer(Long answerId, Long currentUserId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        if (!answer.getAuthor().getId().equals(currentUserId)) {
            throw new RuntimeException("Only the author can delete this answer!");
        }

        answerRepository.delete(answer);
    }
}
