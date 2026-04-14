package com.utcn.demo.service;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

// Teste unitare pentru AnswerService.
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

    // ========================================================================================
    // TEST: addAnswer_ShouldSaveAnswer_WhenTopicExistsAndNotSolved
    // ========================================================================================
    // Ce testăm: Că un răspuns se salvează corect și statusul topic-ului devine IN_PROGRESS.
    //
    // Ce trebuie să faci:
    // 1. Arrange:
    //    when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
    //    when(answerRepository.save(any(Answer.class))).thenReturn(answer);
    //
    // 2. Act: AnswerResponseDTO saved = answerService.addAnswer(1L, answer);
    //
    // 3. Assert:
    //    assertNotNull(saved);
    //    verify(topicRepository).save(topic); → Topic-ul a fost salvat (status schimbat)?
    //    verify(answerRepository).save(answer); → Răspunsul a fost salvat?
    //    assertEquals("IN_PROGRESS", topic.getStatus()); → Statusul s-a schimbat?
    @Test
    void addAnswer_ShouldSaveAnswer_WhenTopicExistsAndNotSolved() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: addAnswer_ShouldThrowException_WhenTopicIsSolved
    // ========================================================================================
    // Ce testăm: Că nu se pot adăuga răspunsuri la un topic deja rezolvat (SOLVED).
    //
    // Ce trebuie să faci:
    // 1. Arrange: topic.setStatus("SOLVED"); + findById returnează topic-ul
    // 2. Act + Assert:
    //    assertThrows + verifică mesajul excepției
    //    verify(answerRepository, never()).save(any()); → Save NU a fost apelat
    @Test
    void addAnswer_ShouldThrowException_WhenTopicIsSolved() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: getAnswersByTopic_ShouldReturnList
    // ========================================================================================
    @Test
    void getAnswersByTopic_ShouldReturnList() {
        // TODO: Implementează
        // Arrange: findById + findByTopicOrderByCreatedAtDesc returnează lista
        // Assert: lista nu e goală, are 1 element
    }

    // ========================================================================================
    // TEST: updateAnswer_ShouldUpdate_WhenUserIsAuthor
    // ========================================================================================
    @Test
    void updateAnswer_ShouldUpdate_WhenUserIsAuthor() {
        // TODO: Implementează
        // Arrange: findById, save
        // Act: updateAnswer(1L, "New text", 1L)
        // Assert: textContent e "New text"
    }

    // ========================================================================================
    // TEST: updateAnswer_ShouldThrowException_WhenUserIsNotAuthor
    // ========================================================================================
    @Test
    void updateAnswer_ShouldThrowException_WhenUserIsNotAuthor() {
        // TODO: Implementează
        // currentUserId = 2L ≠ author.id = 1L → excepție
    }

    // ========================================================================================
    // TEST: deleteAnswer_ShouldDelete_WhenUserIsAuthor
    // ========================================================================================
    @Test
    void deleteAnswer_ShouldDelete_WhenUserIsAuthor() {
        // TODO: Implementează
        // Arrange: findById, doNothing pe delete
        // Act: assertDoesNotThrow(...)
        // Assert: verify delete a fost apelat
    }
}
