package com.utcn.demo.controller;

import com.utcn.demo.entity.Topic;
import com.utcn.demo.service.TopicService;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Topic> getAllTopics() {
        return topicService.getAllTopicsSorted();
    }

    @GetMapping("/search")
    public List<Topic> searchTopics(@RequestParam String title) {
        return topicService.searchByTitle(title);
    }

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        return topicService.createTopic(topic);
    }

    @PutMapping("/{id}")
    public Topic updateTopic(@PathVariable Long id, @RequestParam(required = false) String title,
            @RequestParam(required = false) String content, Principal principal) {
        Long authorId = userService.findByUsername(principal.getName()).orElseThrow().getId();
        return topicService.updateTopic(id, title, content, authorId);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id, Principal principal) {
        Long authorId = userService.findByUsername(principal.getName()).orElseThrow().getId();
        topicService.deleteTopic(id, authorId);
    }
}
