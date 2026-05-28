package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.AnswerResponseDTO;
import com.utcn.demo.entity.Answer;
import com.utcn.demo.service.AnswerService;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@lombok.RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final UserService userService;
    private final com.utcn.demo.service.VoteService voteService;

    @GetMapping("/topic/{topicId}")
    public List<AnswerResponseDTO> getAnswersForTopic(@PathVariable Long topicId) {
        return answerService.getAnswersByTopic(topicId);
    }

    @PostMapping("/topic/{topicId}")
    public AnswerResponseDTO addAnswer(@PathVariable Long topicId, @RequestBody com.utcn.demo.dto.Requests.AnswerRequestDTO answer, Principal principal) {
        return userService.findByUsername(principal.getName())
                .map(user -> answerService.addAnswer(topicId, answer, user.id()))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @PutMapping("/{id}")
    public AnswerResponseDTO updateAnswer(@PathVariable Long id, @RequestBody com.utcn.demo.dto.Requests.AnswerRequestDTO dto, Principal principal) {
        return userService.findByUsername(principal.getName())
                .map(user -> answerService.updateAnswer(id, dto, user.id()))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @PostMapping("/{id}/vote")
    public void voteAnswer(@PathVariable Long id, @RequestParam String voteType, Principal principal) {
        userService.findByUsername(principal.getName())
                .ifPresentOrElse(
                        user -> voteService.voteAnswer(id, user.id(), voteType),
                        () -> { throw new RuntimeException("Sesiune invalida"); }
                );
    }

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id, Principal principal) {
        userService.findByUsername(principal.getName())
                .ifPresentOrElse(
                        user -> answerService.deleteAnswer(id, user.id()),
                        () -> { throw new RuntimeException("Sesiune invalida"); }
                );
    }
}
