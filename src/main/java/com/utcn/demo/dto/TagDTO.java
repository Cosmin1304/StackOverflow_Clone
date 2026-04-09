package com.utcn.demo.dto;

import com.utcn.demo.entity.Tag;

public record TagDTO(Long id, String name) {
    public static TagDTO fromEntity(Tag tag) {
        if (tag == null)
            return null;
        return new TagDTO(tag.getId(), tag.getName());
    }
}
