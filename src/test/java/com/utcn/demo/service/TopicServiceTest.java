package com.utcn.demo.service;

import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.dto.TopicDTO;
import com.utcn.demo.repository.TagRepository;
import com.utcn.demo.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TopicService topicService;

    private Topic topic;
    private User author;
    private Tag tag;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);

        tag = new Tag();
        tag.setName("spring");

        topic = new Topic();
        topic.setId(1L);
        topic.setAuthor(author);
        topic.setTags(Set.of(tag));
    }

    @Test
    void createTopic_ShouldSaveTopicWithManagedTags() {
        when(tagRepository.findByName("spring")).thenReturn(Optional.of(tag));
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        TopicDTO savedTopicDTO = topicService.createTopic(topic);

        assertNotNull(savedTopicDTO);
        assertEquals("RECEIVED", topic.getStatus());
        verify(tagRepository, never()).save(any());
        verify(topicRepository).save(topic);
    }

    @Test
    void getAllTopicsSorted_ShouldReturnList() {
        when(topicRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(topic));

        List<TopicDTO> topics = topicService.getAllTopicsSorted();

        assertFalse(topics.isEmpty());
    }

    @Test
    void getTopicsByTag_ShouldReturnList() {
        when(topicRepository.findByTags_Name("spring")).thenReturn(List.of(topic));

        List<TopicDTO> topics = topicService.getTopicsByTag("spring");

        assertFalse(topics.isEmpty());
    }

    @Test
    void searchByTitle_ShouldReturnList() {
        when(topicRepository.findByTitleContainingIgnoreCase("test")).thenReturn(List.of(topic));

        List<TopicDTO> topics = topicService.searchByTitle("test");

        assertFalse(topics.isEmpty());
    }

    @Test
    void getTopicsByAuthor_ShouldReturnList() {
        when(topicRepository.findByAuthor(author)).thenReturn(List.of(topic));

        List<TopicDTO> topics = topicService.getTopicsByAuthor(author);

        assertNotNull(topics);
        assertFalse(topics.isEmpty());
        assertEquals(1, topics.size());
        assertEquals(topic.getId(), topics.get(0).id());

        verify(topicRepository, times(1)).findByAuthor(author);
    }

    @Test
    void deleteTopic_ShouldDelete_WhenUserIsAuthor() {
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        doNothing().when(topicRepository).delete(topic);

        assertDoesNotThrow(() -> topicService.deleteTopic(1L, 1L));

        verify(topicRepository).delete(topic);
    }

    @Test
    void deleteTopic_ShouldThrowException_WhenUserIsNotAuthor() {
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        assertThrows(RuntimeException.class, () -> {
            topicService.deleteTopic(1L, 2L);
        });
    }

    @Test
    void updateTopic_ShouldUpdateFields_WhenUserIsAuthor() {
        String newTitle = "Titlu Actualizat";
        String newContent = "Continut nou pentru test";

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TopicDTO updatedTopicDTO = topicService.updateTopic(1L, newTitle, newContent, 1L);

        assertNotNull(updatedTopicDTO);
        assertEquals(newTitle, updatedTopicDTO.title());
        assertEquals(newContent, updatedTopicDTO.textContent());
        verify(topicRepository).save(topic);
    }

    @Test
    void updateTopic_ShouldThrowException_WhenUserIsNotAuthor() {
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            topicService.updateTopic(1L, "Titlu", "Continut", 2L);
        });

        assertEquals("Only the author can edit this topic", exception.getMessage());
        verify(topicRepository, never()).save(any());
    }

    @Test
    void updateTopic_ShouldThrowException_WhenTopicNotFound() {
        when(topicRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            topicService.updateTopic(99L, "Titlu", "Continut", 1L);
        });
    }

    @Test
    void updateTopic_ShouldNotUpdate_WhenFieldsAreEmpty() {
        String originalTitle = "Original Title";
        topic.setTitle(originalTitle);

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        topicService.updateTopic(1L, "   ", null, 1L);

        assertEquals(originalTitle, topic.getTitle());
        verify(topicRepository).save(topic);
    }
}
