package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.TagResponseDTO;
import com.utcn.demo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@lombok.RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public List<TagResponseDTO> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping("/{tagName}")
    public TagResponseDTO addTag(@PathVariable String tagName) {
        return tagService.findOrCreateTag(tagName);
    }
}
