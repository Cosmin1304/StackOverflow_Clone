package com.utcn.demo.service;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.dto.AnswerDTO;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private AnswerService answerService;

    private Topic topic;
    private Answer answer;
    private User author;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);

        topic = new Topic();
        topic.setId(1L);
        topic.setStatus("RECEIVED");
        topic.setAuthor(author);

        answer = new Answer();
        answer.setId(1L);
        answer.setAuthor(author);
        answer.setTopic(topic);
    }

    @Test
    void addAnswer_ShouldSaveAnswer_WhenTopicExistsAndNotSolved() {
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        AnswerDTO savedAnswerDTO = answerService.addAnswer(1L, answer);

        assertNotNull(savedAnswerDTO);
        verify(topicRepository).save(topic);
        verify(answerRepository).save(answer);
        assertEquals("IN_PROGRESS", topic.getStatus());
    }

    @Test
    void addAnswer_ShouldThrowException_WhenTopicIsSolved() {
        topic.setStatus("SOLVED");
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            answerService.addAnswer(1L, answer);
        });

        assertEquals("This topic is already solved, no more answers can be added", exception.getMessage());
        verify(answerRepository, never()).save(any());
    }

    @Test
    void getAnswersByTopic_ShouldReturnList() {
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(answerRepository.findByTopicOrderByCreatedAtDesc(topic)).thenReturn(List.of(answer));

        List<AnswerDTO> answers = answerService.getAnswersByTopic(1L);

        assertFalse(answers.isEmpty());
        assertEquals(1, answers.size());
    }

    @Test
    void updateAnswer_ShouldUpdate_WhenUserIsAuthor() {
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        AnswerDTO updatedAnswerDTO = answerService.updateAnswer(1L, "New text", 1L);

        assertNotNull(updatedAnswerDTO);
        assertEquals("New text", answer.getTextContent());
    }

    @Test
    void updateAnswer_ShouldThrowException_WhenUserIsNotAuthor() {
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        assertThrows(RuntimeException.class, () -> {
            answerService.updateAnswer(1L, "New text", 2L);
        });
    }

    @Test
    void deleteAnswer_ShouldDelete_WhenUserIsAuthor() {
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));
        doNothing().when(answerRepository).delete(answer);

        assertDoesNotThrow(() -> answerService.deleteAnswer(1L, 1L));

        verify(answerRepository).delete(answer);
    }
}
