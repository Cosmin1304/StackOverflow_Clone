package com.utcn.demo.service;

import com.utcn.demo.entity.Tag;
import com.utcn.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.utcn.demo.dto.TagDTO;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public TagDTO findOrCreateTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setName(tagName);
            return tagRepository.save(newTag);
        });
        return TagDTO.fromEntity(tag);
    }

    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        tagRepository.findAll().forEach(tags::add);
        return tags.stream().map(TagDTO::fromEntity).collect(Collectors.toList());
    }

}
