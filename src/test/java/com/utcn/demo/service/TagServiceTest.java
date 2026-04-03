package com.utcn.demo.service;

import com.utcn.demo.entity.Tag;
import com.utcn.demo.dto.TagDTO;
import com.utcn.demo.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void findOrCreateTag_ShouldReturnExistingTag_WhenTagExists() {
        when(tagRepository.findByName("java")).thenReturn(Optional.of(tag));

        TagDTO result = tagService.findOrCreateTag("java");

        assertEquals(tag.getId(), result.id());
        verify(tagRepository, never()).save(any());
    }

    @Test
    void findOrCreateTag_ShouldCreateAndReturnNewTag_WhenTagDoesNotExist() {
        when(tagRepository.findByName("python")).thenReturn(Optional.empty());

        Tag newTag = new Tag();
        newTag.setName("python");
        when(tagRepository.save(any(Tag.class))).thenReturn(newTag);

        TagDTO result = tagService.findOrCreateTag("python");

        assertEquals("python", result.name());
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void getAllTags_ShouldReturnList() {
        when(tagRepository.findAll()).thenReturn(List.of(tag));

        List<TagDTO> tags = tagService.getAllTags();

        assertFalse(tags.isEmpty());
        assertEquals(1, tags.size());
    }
}
