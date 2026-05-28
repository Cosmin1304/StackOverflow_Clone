package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.TopicResponseDTO;
import com.utcn.demo.service.TopicService;
import com.utcn.demo.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
@lombok.RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;
    private final com.utcn.demo.service.VoteService voteService;

    @GetMapping
    public List<TopicResponseDTO> getAllTopics() {
        return topicService.getAllTopicsSorted();
    }

    @GetMapping("/search")
    public List<TopicResponseDTO> searchTopics(@RequestParam String title) {
        return topicService.searchByTitle(title);
    }

    @PostMapping
    public TopicResponseDTO createTopic(@RequestBody com.utcn.demo.dto.Requests.TopicRequestDTO topic, Principal principal) {
        if (principal == null) throw new RuntimeException("Sesiune invalida (utilizator neautentificat)");

        return userService.findUserEntityByUsername(principal.getName())
                .map(user -> topicService.createTopic(topic, user))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @PutMapping("/{id}")
    public TopicResponseDTO updateTopic(@PathVariable Long id, @RequestBody com.utcn.demo.dto.Requests.TopicRequestDTO topic, Principal principal) {
        if (principal == null) throw new RuntimeException("Sesiune invalida");

        return userService.findByUsername(principal.getName())
                .map(user -> topicService.updateTopic(id, topic, user.id()))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @GetMapping("/tag/{tagName}")
    public List<TopicResponseDTO> getTopicsByTag(@PathVariable String tagName) {
        return topicService.getTopicsByTag(tagName);
    }

    @GetMapping("/author/{username}")
    public List<TopicResponseDTO> getTopicsByAuthor(@PathVariable String username) {
        com.utcn.demo.dto.Requests.UserRequestDTO authorDto = new com.utcn.demo.dto.Requests.UserRequestDTO(username, null, null, null);
        return topicService.getTopicsByAuthor(authorDto);
    }

    @GetMapping("/{id}")
    public TopicResponseDTO getTopicById(@PathVariable Long id) {
        System.out.println("Backend a primit cerere pentru ID: " + id);
        TopicResponseDTO result = topicService.getTopicById(id);
        System.out.println("Rezultatul trimis spre Frontend: " + result);
        return result;
    }

    @PutMapping("/{id}/accept-answer")
    public TopicResponseDTO acceptAnswer(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Sesiune invalida");

        return userService.findByUsername(principal.getName())
                .map(user -> topicService.acceptAnswer(id, user.id()))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @PostMapping("/{id}/vote")
    public void voteTopic(@PathVariable Integer id, @RequestParam String voteType, Principal principal) {
        if (principal == null) throw new RuntimeException("Sesiune invalida");

        userService.findByUsername(principal.getName())
                .ifPresentOrElse(
                        user -> voteService.voteTopic(id, user.id(), voteType),
                        () -> { throw new RuntimeException("Sesiune invalida"); }
                );
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id, Principal principal) {
        if (principal == null) throw new RuntimeException("Sesiune invalida");

        userService.findByUsername(principal.getName())
                .ifPresentOrElse(
                        user -> topicService.deleteTopic(id, user.id()),
                        () -> { throw new RuntimeException("Sesiune invalida"); }
                );
    }
}