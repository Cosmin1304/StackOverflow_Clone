package com.utcn.demo.service;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utcn.demo.dto.Responses.AnswerResponseDTO;

import java.util.List;

// AnswerService — conține logica de business pentru răspunsuri (Answer).
// Un Answer este un răspuns la un Topic (întrebare pe forum).
@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TopicRepository topicRepository;

    // ========================================================================================
    // METODA: addAnswer
    // ========================================================================================
    // Scop: Adaugă un răspuns la un topic existent.
    //
    // Parametri:
    //   - topicId: ID-ul topic-ului la care se adaugă răspunsul
    //   - answer: entitatea Answer cu datele răspunsului (textContent, author setat deja de controller)
    //
    // Ce trebuie să faci:
    // 1. Caută topic-ul în DB: topicRepository.findById(topicId)
    //    Dacă nu există → RuntimeException("Topic not found")
    //
    // 2. Verifică dacă topic-ul NU e deja rezolvat:
    //    Dacă topic.getStatus() == "SOLVED" → aruncă RuntimeException
    //    "This topic is already solved, no more answers can be added"
    //    DE CE: Dacă o întrebare e marcată ca rezolvată, nu mai permitem răspunsuri noi.
    //
    // 3. Setează data creării: answer.setCreatedAt(LocalDateTime.now())
    //
    // 4. Leagă răspunsul de topic: answer.setTopic(topic)
    //    DE CE: Answer are o relație @ManyToOne cu Topic. Trebuie setată explicit.
    //
    // 5. Verifică dacă acesta e PRIMUL răspuns:
    //    Dacă topic.getAnswers().isEmpty() ȘI statusul e "RECEIVED"
    //    → Schimbă statusul topic-ului în "IN_PROGRESS"
    //    → Salvează topic-ul: topicRepository.save(topic)
    //    DE CE: Primul răspuns înseamnă că cineva a început să lucreze la întrebare.
    //
    // 6. Salvează răspunsul: answerRepository.save(answer)
    // 7. Returnează AnswerResponseDTO.fromEntity(...)
    @Transactional
    public AnswerResponseDTO addAnswer(Long topicId, Answer answer) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: getAnswersByTopic
    // ========================================================================================
    // Scop: Returnează toate răspunsurile pentru un topic, sortate descrescător după data creării.
    //
    // Ce trebuie să faci:
    // 1. Caută topic-ul: topicRepository.findById(topicId)
    //    Dacă nu există → RuntimeException("Topic not found")
    // 2. Apelează answerRepository.findByTopicOrderByCreatedAtDesc(topic)
    // 3. Convertește în DTO-uri cu stream/map/collect.
    @Transactional(readOnly = true)
    public List<AnswerResponseDTO> getAnswersByTopic(Long topicId) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: updateAnswer
    // ========================================================================================
    // Scop: Actualizează textul unui răspuns, doar de către autorul său.
    //
    // Parametri:
    //   - answerId: ID-ul răspunsului
    //   - newText: noul text al răspunsului
    //   - currentUserId: ID-ul utilizatorului autentificat
    //
    // Ce trebuie să faci:
    // 1. Caută răspunsul: answerRepository.findById(answerId)
    //    Dacă nu există → RuntimeException("Answer not found")
    //
    // 2. Verifică permisiunea: answer.getAuthor().getId() trebuie să fie EGAL cu currentUserId
    //    Dacă NU → RuntimeException("Only the author can edit this answer!")
    //
    // 3. Actualizează textul: answer.setTextContent(newText)
    // 4. Salvează: answerRepository.save(answer)
    // 5. Returnează AnswerResponseDTO.fromEntity(...)
    @Transactional
    public AnswerResponseDTO updateAnswer(Long answerId, String newText, Long currentUserId) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: deleteAnswer
    // ========================================================================================
    // Scop: Șterge un răspuns, doar de către autorul său.
    //
    // Ce trebuie să faci:
    // 1. Caută răspunsul: answerRepository.findById(answerId)
    // 2. Verifică permisiunea (la fel ca la update)
    // 3. Șterge: answerRepository.delete(answer)
    public void deleteAnswer(Long answerId, Long currentUserId) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }
}
