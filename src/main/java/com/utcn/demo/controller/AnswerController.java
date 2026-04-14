package com.utcn.demo.controller;

import com.utcn.demo.dto.Responses.AnswerResponseDTO;
import com.utcn.demo.entity.Answer;
import com.utcn.demo.service.AnswerService;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

// AnswerController — primește request-uri HTTP legate de răspunsuri.
// Răspunsurile sunt mereu legate de un topic (relație @ManyToOne).
// Endpoint-urile sunt sub /api/answers.
@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    // ========================================================================================
    // ENDPOINT: GET /api/answers/topic/{topicId}
    // ========================================================================================
    // Scop: Returnează toate răspunsurile pentru un topic specificat prin ID.
    //
    // Ce trebuie să faci:
    // 1. Apelează answerService.getAnswersByTopic(topicId) și returnează lista.
    @GetMapping("/topic/{topicId}")
    public List<AnswerResponseDTO> getAnswersForTopic(@PathVariable Long topicId) {
        // TODO: Implementează
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: POST /api/answers/topic/{topicId}
    // ========================================================================================
    // Scop: Adaugă un răspuns nou la un topic.
    //
    // Ce trebuie să faci:
    // 1. Extrage autorul din sesiune:
    //    User author = userService.findUserEntityByUsername(principal.getName())
    //       .orElseThrow(() -> new RuntimeException("Sesiune invalida"));
    //    DE CE: Răspunsul trebuie să aibă un autor. Autorul e user-ul logat,
    //    nu un ID trimis de client (ar fi nesigur — clientul ar putea trimite orice ID).
    //
    // 2. Setează autorul pe answer: answer.setAuthor(author)
    //
    // 3. Apelează answerService.addAnswer(topicId, answer) și returnează rezultatul.
    @PostMapping("/topic/{topicId}")
    public AnswerResponseDTO addAnswer(@PathVariable Long topicId, @RequestBody Answer answer, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: PUT /api/answers/{id}?newText=...
    // ========================================================================================
    // Scop: Actualizează textul unui răspuns (doar autorul poate face asta).
    //
    // Ce trebuie să faci:
    // 1. Extrage ID-ul autorului curent din Principal (findByUsername → .id())
    // 2. Apelează answerService.updateAnswer(id, newText, authorId)
    // 3. Returnează rezultatul.
    @PutMapping("/{id}")
    public AnswerResponseDTO updateAnswer(@PathVariable Long id, @RequestParam String newText, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // ENDPOINT: DELETE /api/answers/{id}
    // ========================================================================================
    // Scop: Șterge un răspuns (doar autorul poate face asta).
    //
    // Ce trebuie să faci:
    // 1. Extrage ID-ul autorului curent
    // 2. Apelează answerService.deleteAnswer(id, authorId)
    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id, Principal principal) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }
}
