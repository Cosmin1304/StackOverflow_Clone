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
public class TagController {

    @Autowired
    private TagService tagService;

    // ========================================================================================
    // ENDPOINT: GET /api/tags
    // ========================================================================================
    // Scop: Returnează toate tag-urile existente.
    //
    // Ce trebuie să faci:
    // 1. Apelează tagService.getAllTags() și returnează lista.
    @GetMapping
    public List<TagResponseDTO> getAllTags() {
        // TODO: Implementează
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: POST /api/tags/{tagName}
    // ========================================================================================
    // Scop: Creează un tag nou SAU returnează tag-ul existent dacă deja există cu acel nume.
    //
    // @PathVariable String tagName — numele tag-ului vine din URL:
    //   Ex: POST /api/tags/spring → tagName = "spring"
    //
    // Ce trebuie să faci:
    // 1. Apelează tagService.findOrCreateTag(tagName) și returnează rezultatul.
    @PostMapping("/{tagName}")
    public TagResponseDTO addTag(@PathVariable String tagName) {
        // TODO: Implementează
        throw new UnsupportedOperationException("De implementat");
    }
}
