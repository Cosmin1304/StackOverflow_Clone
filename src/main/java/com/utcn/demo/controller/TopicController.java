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
public class TopicController {

    @Autowired
    private TopicService topicService;

    // Avem nevoie de UserService pentru a extrage entitatea/ID-ul user-ului autentificat
    // din obiectul Principal (care conține doar username-ul).
    @Autowired
    private UserService userService;

    // ========================================================================================
    // ENDPOINT: GET /api/topics
    // ========================================================================================
    // Scop: Returnează toate topic-urile, sortate descrescător după data creării.
    //
    // Ce trebuie să faci:
    // 1. Apelează topicService.getAllTopicsSorted() și returnează lista.
    @GetMapping
    public List<TopicResponseDTO> getAllTopics() {
        // TODO: Implementează
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: GET /api/topics/search?title=...
    // ========================================================================================
    // Scop: Caută topic-uri al căror titlu conține textul specificat.
    //
    // @RequestParam — extrage parametrul din query string:
    //   Ex: GET /api/topics/search?title=spring → title = "spring"
    //   Diferența față de @PathVariable: PathVariable e în URL (/topics/{id}),
    //   RequestParam e după ? (/topics/search?title=...)
    //
    // Ce trebuie să faci:
    // 1. Apelează topicService.searchByTitle(title) și returnează.
    @GetMapping("/search")
    public List<TopicResponseDTO> searchTopics(@RequestParam String title) {
        // TODO: Implementează
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: POST /api/topics
    // ========================================================================================
    // Scop: Creează un topic nou. Autorul se setează automat din sesiunea autentificată.
    //
    // Ce trebuie să faci:
    // 1. Extrage autorul (entitatea User) din sesiune:
    //    User author = userService.findUserEntityByUsername(principal.getName())
    //       .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    //    DE CE findUserEntityByUsername (nu findByUsername)?
    //    Aici avem nevoie de entitatea User completă (nu DTO) pentru a o seta pe topic.
    //    JPA are nevoie de entitatea managed pentru relația @ManyToOne.
    //
    // 2. Setează autorul pe topic: topic.setAuthor(author)
    //
    // 3. Apelează topicService.createTopic(topic) și returnează rezultatul.
    @PostMapping
    public TopicResponseDTO createTopic(@RequestBody Topic topic, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: PUT /api/topics/{id}?title=...&content=...
    // ========================================================================================
    // Scop: Actualizează titlul și/sau conținutul unui topic (doar autorul poate face asta).
    //
    // @RequestParam(required = false) — parametrii sunt opționali.
    //   Poți trimite doar titlul, doar content-ul, sau amândouă.
    //
    // Ce trebuie să faci:
    // 1. Extrage ID-ul autorului curent din Principal (prin findByUsername → .id())
    // 2. Apelează topicService.updateTopic(id, title, content, authorId)
    // 3. Returnează rezultatul.
    @PutMapping("/{id}")
    public TopicResponseDTO updateTopic(@PathVariable Long id, @RequestParam(required = false) String title,
                                        @RequestParam(required = false) String content, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: DELETE /api/topics/{id}
    // ========================================================================================
    // Scop: Șterge un topic (doar autorul poate face asta).
    //
    // Ce trebuie să faci:
    // 1. Extrage ID-ul autorului curent (la fel ca la update)
    // 2. Apelează topicService.deleteTopic(id, authorId)
    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }
}
