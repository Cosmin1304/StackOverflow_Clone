package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.TopicResponseDTO;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.service.TopicService;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

// TopicController — primește request-uri HTTP legate de topic-uri (întrebări pe forum).
// Endpoint-urile sunt sub /api/topics.
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
        return userService.findUserEntityByUsername(principal.getName())
                .map(user -> topicService.createTopic(topic))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @PutMapping("/{id}")
    public TopicResponseDTO updateTopic(@PathVariable Long id, @RequestBody com.utcn.demo.dto.Requests.TopicRequestDTO topic, Principal principal) {
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
        com.utcn.demo.dto.Requests.UserRequestDTO authorDto = new com.utcn.demo.dto.Requests.UserRequestDTO(username, null, null);
        return topicService.getTopicsByAuthor(authorDto);
    }

    @PutMapping("/{id}/accept-answer")
    public TopicResponseDTO acceptAnswer(@PathVariable Long id, Principal principal) {
        return userService.findByUsername(principal.getName())
                .map(user -> topicService.acceptAnswer(id, user.id()))
                .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    }

    @PostMapping("/{id}/vote")
    public void voteTopic(@PathVariable Integer id, @RequestParam String voteType, Principal principal) {
        userService.findByUsername(principal.getName())
                .ifPresentOrElse(
                        user -> voteService.voteTopic(id, user.id(), voteType),
                        () -> { throw new RuntimeException("Sesiune invalida"); }
                );
    }



    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id, Principal principal) {
        userService.findByUsername(principal.getName())
                .ifPresentOrElse(
                        user -> topicService.deleteTopic(id, user.id()),
                        () -> { throw new RuntimeException("Sesiune invalida"); }
                );
    }
}
