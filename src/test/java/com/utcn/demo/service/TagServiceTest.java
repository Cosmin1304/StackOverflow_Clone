package com.utcn.demo.service;

import com.utcn.demo.entity.Tag;
import com.utcn.demo.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

// Teste unitare pentru TagService.
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("java");
    }

    // ========================================================================================
    // TEST: findOrCreateTag_ShouldReturnExistingTag_WhenTagExists
    // ========================================================================================
    // Ce testăm: Că dacă tag-ul "java" există deja, se returnează fără a crea unul nou.
    //
    // Ce trebuie să faci:
    // 1. Arrange: when(tagRepository.findByName("java")).thenReturn(Optional.of(tag));
    // 2. Act: TagResponseDTO result = tagService.findOrCreateTag("java");
    // 3. Assert:
    //    assertEquals(tag.getId(), result.id()); → ID-ul se potrivește?
    //    verify(tagRepository, never()).save(any()); → save NU a fost apelat (tag-ul exista deja)
    @Test
    void findOrCreateTag_ShouldReturnExistingTag_WhenTagExists() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: findOrCreateTag_ShouldCreateAndReturnNewTag_WhenTagDoesNotExist
    // ========================================================================================
    // Ce testăm: Că dacă tag-ul "python" NU există, se creează și se salvează.
    //
    // Ce trebuie să faci:
    // 1. Arrange:
    //    when(tagRepository.findByName("python")).thenReturn(Optional.empty()); → nu există
    //    Tag newTag = new Tag(); newTag.setName("python");
    //    when(tagRepository.save(any(Tag.class))).thenReturn(newTag);
    //
    // 2. Act: TagResponseDTO result = tagService.findOrCreateTag("python");
    //
    // 3. Assert:
    //    assertEquals("python", result.name());
    //    verify(tagRepository).save(any(Tag.class)); → save A fost apelat
    @Test
    void findOrCreateTag_ShouldCreateAndReturnNewTag_WhenTagDoesNotExist() {
        // TODO: Implementează
    }

    // ========================================================================================
    // TEST: getAllTags_ShouldReturnList
    // ========================================================================================
    @Test
    void getAllTags_ShouldReturnList() {
        // TODO: Implementează
        // Arrange: when(tagRepository.findAll()).thenReturn(List.of(tag));
        // Act: apelează getAllTags()
        // Assert: lista nu e goală, are 1 element
    }
}
