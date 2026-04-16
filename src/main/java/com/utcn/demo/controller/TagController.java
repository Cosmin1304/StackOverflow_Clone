package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.TagResponseDTO;
import com.utcn.demo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TagController — primește request-uri HTTP legate de tag-uri.
// Tag-urile sunt etichete care se atașează pe topic-uri (ex: "java", "spring", "sql").
// Endpoint-urile sunt sub /api/tags.
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
