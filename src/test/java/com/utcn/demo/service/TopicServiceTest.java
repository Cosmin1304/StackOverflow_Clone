package com.utcn.demo.service;

import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TagRepository;
import com.utcn.demo.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

// Teste unitare pentru TopicService.
// Testăm scenariile: creare topic, listare, căutare, ștergere, update — inclusiv cazurile de eroare.
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

    // ========================================================================================
    // TEST: createTopic_ShouldSaveTopicWithManagedTags
    // ========================================================================================
    // Ce testăm: Că la creare, tag-urile existente se reutilizează (nu se creează duplicate).
    //
    // Ce trebuie să faci:
    // 1. Arrange:
    //    when(tagRepository.findByName("spring")).thenReturn(Optional.of(tag));
    //    → Tag-ul "spring" există deja → se reutilizează
    //    when(topicRepository.save(any(Topic.class))).thenReturn(topic);
    //
    // 2. Act: TopicResponseDTO savedTopicDTO = topicService.createTopic(topic);
    //
    // 3. Assert:
    //    assertNotNull(savedTopicDTO);
    //    assertEquals("RECEIVED", topic.getStatus()); → Statusul inițial e RECEIVED?
    //    verify(tagRepository, never()).save(any()); → Tag-ul NU a fost salvat din nou?
    //    verify(topicRepository).save(topic); → Topic-ul A fost salvat?
    @Test
    void createTopic_ShouldSaveTopicWithManagedTags() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: getAllTopicsSorted_ShouldReturnList
    // ========================================================================================
    // Ce trebuie să faci:
    // 1. Arrange: when(topicRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(topic));
    // 2. Act: List<TopicResponseDTO> topics = topicService.getAllTopicsSorted();
    // 3. Assert: assertFalse(topics.isEmpty());
    @Test
    void getAllTopicsSorted_ShouldReturnList() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: getTopicsByTag_ShouldReturnList
    // ========================================================================================
    // Ce trebuie să faci:
    // 1. Arrange: when(topicRepository.findByTags_Name("spring")).thenReturn(List.of(topic));
    // 2. Act + Assert: lista nu e goală.
    @Test
    void getTopicsByTag_ShouldReturnList() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: searchByTitle_ShouldReturnList
    // ========================================================================================
    @Test
    void searchByTitle_ShouldReturnList() {
        // TODO: Implementează — similar cu testele de mai sus, folosește findByTitleContainingIgnoreCase
    }

    // ========================================================================================
    // TEST: getTopicsByAuthor_ShouldReturnList
    // ========================================================================================
    // Ce trebuie să faci:
    // 1. Arrange: when(topicRepository.findByAuthor(author)).thenReturn(List.of(topic));
    // 2. Act: apelează getTopicsByAuthor(author)
    // 3. Assert: lista nu e goală, are 1 element, id-ul se potrivește
    //    verify(topicRepository, times(1)).findByAuthor(author);
    //    → times(1) = confirmă că metoda a fost apelată EXACT o dată
    @Test
    void getTopicsByAuthor_ShouldReturnList() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: deleteTopic_ShouldDelete_WhenUserIsAuthor
    // ========================================================================================
    // Ce trebuie să faci:
    // 1. Arrange: findById returnează topic-ul, doNothing pe delete
    // 2. Act: assertDoesNotThrow(() -> topicService.deleteTopic(1L, 1L));
    //    assertDoesNotThrow = confirmă că NU se aruncă excepție
    // 3. Assert: verify(topicRepository).delete(topic);
    @Test
    void deleteTopic_ShouldDelete_WhenUserIsAuthor() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: deleteTopic_ShouldThrowException_WhenUserIsNotAuthor
    // ========================================================================================
    // Ce trebuie să faci:
    // 1. Arrange: findById returnează topic-ul (author.id = 1)
    // 2. Act + Assert: assertThrows(RuntimeException.class, () -> topicService.deleteTopic(1L, 2L));
    //    → currentUserId = 2 ≠ author.id = 1 → excepție
    @Test
    void deleteTopic_ShouldThrowException_WhenUserIsNotAuthor() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: updateTopic_ShouldUpdateFields_WhenUserIsAuthor
    // ========================================================================================
    // Ce trebuie să faci:
    // 1. Arrange:
    //    when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
    //    when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));
    //    → thenAnswer returnează EXACT obiectul primit ca argument (topic-ul modificat)
    //
    // 2. Act: TopicResponseDTO updated = topicService.updateTopic(1L, "Titlu Actualizat", "Continut nou", 1L);
    //
    // 3. Assert: titlul și conținutul s-au schimbat, save a fost apelat
    @Test
    void updateTopic_ShouldUpdateFields_WhenUserIsAuthor() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: updateTopic_ShouldThrowException_WhenUserIsNotAuthor
    // ========================================================================================
    @Test
    void updateTopic_ShouldThrowException_WhenUserIsNotAuthor() {
        // TODO: Implementează — similar cu deleteTopic not author
        // Verifică și mesajul excepției: assertEquals("Only the author can edit this topic", ...)
        // Verifică că save NU a fost apelat: verify(topicRepository, never()).save(any())
    }

    // ========================================================================================
    // TEST: updateTopic_ShouldThrowException_WhenTopicNotFound
    // ========================================================================================
    @Test
    void updateTopic_ShouldThrowException_WhenTopicNotFound() {
        // TODO: Implementează
        // Arrange: when(topicRepository.findById(99L)).thenReturn(Optional.empty());
        // Assert: assertThrows(RuntimeException.class, ...)
    }

    // ========================================================================================
    // TEST: updateTopic_ShouldNotUpdate_WhenFieldsAreEmpty
    // ========================================================================================
    // Ce testăm: Că dacă trimitem titlu gol ("   ") și content null, câmpurile rămân neschimbate.
    //
    // Ce trebuie să faci:
    // 1. Arrange: setează un titlu original pe topic, configurează mock-urile
    // 2. Act: apelează updateTopic cu titlu = "   " și content = null
    // 3. Assert: titlul rămâne cel original, save tot se apelează (dar fără modificări)
    @Test
    void updateTopic_ShouldNotUpdate_WhenFieldsAreEmpty() {
        // TODO: Implementează
    }
}
