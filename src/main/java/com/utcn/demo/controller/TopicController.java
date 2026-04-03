package com.utcn.demo.controller;

import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.TopicService;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import com.utcn.demo.dto.TopicDTO;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<TopicDTO> getAllTopics() {
        return topicService.getAllTopicsSorted();
    }

    @GetMapping("/search")
    public List<TopicDTO> searchTopics(@RequestParam String title) {
        return topicService.searchByTitle(title);
    }

    @PostMapping
    public TopicDTO createTopic(@RequestBody Topic topic, Principal principal) {
        User author = userService.findUserEntityByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
        topic.setAuthor(author);
        return topicService.createTopic(topic);
    }

    @PutMapping("/{id}")
    public TopicDTO updateTopic(@PathVariable Long id, @RequestParam(required = false) String title,
            @RequestParam(required = false) String content, Principal principal) {
        Long authorId = userService.findByUsername(principal.getName()).orElseThrow().id();
        return topicService.updateTopic(id, title, content, authorId);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id, Principal principal) {
        Long authorId = userService.findByUsername(principal.getName()).orElseThrow().id();
        topicService.deleteTopic(id, authorId);
    }
}
