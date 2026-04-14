package com.utcn.demo.dto.Mappers;

import com.utcn.demo.dto.Responses.TagResponseDTO;
import com.utcn.demo.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    // Entity → ResponseDTO
    public TagResponseDTO toResponse(Tag tag) {
        return tag == null ? null :new TagResponseDTO(tag.getId(),tag.getName());
    }
}
