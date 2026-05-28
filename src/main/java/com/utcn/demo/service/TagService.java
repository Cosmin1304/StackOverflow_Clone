package com.utcn.demo.service;

import com.utcn.demo.dto.Mappers.TagMapper;
import com.utcn.demo.dto.Responses.TagResponseDTO;
import com.utcn.demo.entity.Tag;
import com.utcn.demo.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;


    @Transactional
    public TagResponseDTO findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .map(tagMapper::toResponse)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    return tagMapper.toResponse(tagRepository.save(tag));
                        }
                );
    }


    @Transactional(readOnly = true)
    public List<TagResponseDTO> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        tagRepository.findAll().forEach(tags::add);
        return tags.stream().filter(Objects::nonNull).map(tagMapper::toResponse).collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if(list.isEmpty()){
                        throw new RuntimeException("no tags found");
                    }
                    return list;
                }
        ));
    }

}
