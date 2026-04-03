package com.utcn.demo.controller;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.service.AnswerService;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @GetMapping("/topic/{topicId}")
    public List<Answer> getAnswersForTopic(@PathVariable Long topicId) {
        return answerService.getAnswersByTopic(topicId);
    }

    @PostMapping("/topic/{topicId}")
    public Answer addAnswer(@PathVariable Long topicId, @RequestBody Answer answer) {
        return answerService.addAnswer(topicId, answer);
    }

    @PutMapping("/{id}")
    public Answer updateAnswer(@PathVariable Long id, @RequestParam String newText, Principal principal) {
        Long authorId = userService.findByUsername(principal.getName()).orElseThrow().getId();
        return answerService.updateAnswer(id, newText, authorId);
    }

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id, Principal principal) {
        Long authorId = userService.findByUsername(principal.getName()).orElseThrow().getId();
        answerService.deleteAnswer(id, authorId);
    }
}
