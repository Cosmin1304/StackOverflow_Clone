package com.utcn.demo.service;

import com.utcn.demo.dto.Responses.TopicResponseDTO;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

// @Service — marchează clasa ca un Service Spring (bean detectat automat).
// TopicService conține logica de business pentru Topic-uri (întrebări/discuții pe forum).
@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TagRepository tagRepository;

    // ========================================================================================
    // METODA: createTopic
    // ========================================================================================
    // Scop: Creează un topic (întrebare) nou și îl salvează în baza de date.
    //
    // Ce trebuie să faci:
    // 1. Setează data creării: topic.setCreatedAt(LocalDateTime.now())
    //    DE CE: Vrem să știm exact când a fost creat topic-ul (pentru sortare cronologică).
    //
    // 2. Setează statusul inițial: topic.setStatus("RECEIVED")
    //    DE CE: Un topic nou nu are răspunsuri → statusul e "RECEIVED" (primit, nerezolvat).
    //    Ciclul de viață: RECEIVED → IN_PROGRESS (când primește primul răspuns) → SOLVED
    //
    // 3. Gestionează tag-urile:
    //    - Verifică dacă topic.getTags() nu e null
    //    - Pentru FIECARE tag din colecție:
    //      a) Caută tag-ul în DB după nume: tagRepository.findByName(tag.getName())
    //      b) Dacă există → folosește tag-ul existent (nu creăm duplicat)
    //      c) Dacă NU există → salvează tag-ul nou: tagRepository.save(tag)
    //      Folosește .orElseGet(() -> tagRepository.save(tag)) pentru logica b/c.
    //    - Adună toate tag-urile gestionate într-un Set și setează-le pe topic.
    //    DE CE: Evităm duplicate. Dacă tag-ul "java" există deja, îl reutilizăm.
    //
    // 4. Salvează topic-ul: topicRepository.save(topic)
    // 5. Convertește în TopicResponseDTO și returnează.
    @Transactional
    public TopicResponseDTO createTopic(Topic topic) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: getAllTopicsSorted
    // ========================================================================================
    // Scop: Returnează TOATE topic-urile, sortate descrescător după data creării (cele mai noi primele).
    //
    // Ce trebuie să faci:
    // 1. Apelează topicRepository.findAllByOrderByCreatedAtDesc()
    //    Aceasta este o "derived query" — Spring Data generează automat SQL-ul
    //    din numele metodei: "findAll" + "ByOrderBy" + "CreatedAt" + "Desc"
    // 2. Convertește lista de Topic-uri în TopicResponseDTO-uri cu stream/map/collect:
    //    .stream().map(TopicResponseDTO::fromEntity).collect(Collectors.toList())
    @Transactional(readOnly = true)
    public List<TopicResponseDTO> getAllTopicsSorted() {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: getTopicsByTag
    // ========================================================================================
    // Scop: Returnează toate topic-urile care au un anumit tag (ex: "spring", "java").
    //
    // Ce trebuie să faci:
    // 1. Apelează topicRepository.findByTags_Name(tagName)
    //    "Tags_Name" → Spring Data navighează relația ManyToMany din Topic → Tag → name
    // 2. Convertește în DTO-uri cu stream/map/collect.
    @Transactional(readOnly = true)
    public List<TopicResponseDTO> getTopicsByTag(String tagName) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: searchByTitle
    // ========================================================================================
    // Scop: Caută topic-uri al căror titlu conține textul dat (case-insensitive).
    //
    // Ce trebuie să faci:
    // 1. Apelează topicRepository.findByTitleContainingIgnoreCase(title)
    //    "ContainingIgnoreCase" → generează un LIKE '%title%' case-insensitive în SQL.
    // 2. Convertește în DTO-uri.
    @Transactional(readOnly = true)
    public List<TopicResponseDTO> searchByTitle(String title) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: getTopicsByAuthor
    // ========================================================================================
    // Scop: Returnează toate topic-urile create de un anumit utilizator.
    //
    // Ce trebuie să faci:
    // 1. Apelează topicRepository.findByAuthor(author) — caută după entitatea User
    // 2. Convertește în DTO-uri.
    @Transactional(readOnly = true)
    public List<TopicResponseDTO> getTopicsByAuthor(User author) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: deleteTopic
    // ========================================================================================
    // Scop: Șterge un topic, DAR doar dacă persoana care cere ștergerea este autorul topic-ului.
    //
    // Parametri:
    //   - id: ID-ul topic-ului de șters
    //   - currentUserId: ID-ul user-ului autentificat care face cererea
    //
    // Ce trebuie să faci:
    // 1. Caută topic-ul în DB: topicRepository.findById(id)
    //    Dacă nu există → aruncă RuntimeException("topic not found")
    //
    // 2. Verifică permisiunea: currentUserId trebuie să fie EGAL cu topic.getAuthor().getId()
    //    Dacă NU → aruncă RuntimeException("Only the author can delete this topic")
    //    DE CE: Doar autorul ar trebui să-și poată șterge propriul topic.
    //
    // 3. Dacă totul e OK → topicRepository.delete(topic)
    public void deleteTopic(Long id, Long currentUserId) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: updateTopic
    // ========================================================================================
    // Scop: Actualizează titlul și/sau conținutul unui topic, doar de către autor.
    //
    // Parametri:
    //   - topicId: ID-ul topic-ului
    //   - newTitle: noul titlu (poate fi null sau gol → nu se schimbă)
    //   - newTextContent: noul conținut text (poate fi null sau gol → nu se schimbă)
    //   - currentUserId: ID-ul user-ului autentificat
    //
    // Ce trebuie să faci:
    // 1. Caută topic-ul: topicRepository.findById(topicId)
    //    Dacă nu există → RuntimeException("Topic not found")
    //
    // 2. Verifică permisiunea (la fel ca la delete)
    //
    // 3. Actualizează titlul DOAR dacă newTitle nu e null ȘI nu e gol/whitespace:
    //    if (newTitle != null && !newTitle.trim().isEmpty()) → topic.setTitle(newTitle)
    //    DE CE trim()? — Evităm titluri formate doar din spații.
    //
    // 4. Actualizează conținutul cu aceeași logică.
    //
    // 5. Salvează: topicRepository.save(topic)
    // 6. Returnează TopicResponseDTO.fromEntity(...)
    @Transactional
    public TopicResponseDTO updateTopic(Long topicId, String newTitle, String newTextContent, Long currentUserId) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }
}
