package com.utcn.demo.controller;

import com.utcn.demo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.utcn.demo.dto.TagDTO;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<TagDTO> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping("/{tagName}")
    public TagDTO addTag(@PathVariable String tagName) {
        return tagService.findOrCreateTag(tagName);
    }
}
