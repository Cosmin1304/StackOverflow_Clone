package com.utcn.demo.controller;

import com.utcn.demo.service.LocalImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final LocalImageService localImageService;

    @PostMapping("/upload")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = localImageService.uploadImage(file);
        return Map.of("url", imageUrl);
    }
}