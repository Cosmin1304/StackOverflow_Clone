package com.utcn.demo.service;

import com.utcn.demo.dto.Responses.TagResponseDTO;
import com.utcn.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

// TagService — conține logica de business pentru Tag-uri.
// Tag-urile sunt etichete atașate topic-urilor (ex: "java", "spring", "database").
// Permit filtrarea și categorizarea topic-urilor.
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    // ========================================================================================
    // METODA: findOrCreateTag
    // ========================================================================================
    // Scop: Caută un tag după nume. Dacă nu există, îl creează și îl salvează în DB.
    //       Returnează tag-ul (existent sau nou creat) ca TagResponseDTO.
    //
    // Aceasta este o metodă de tip "get or create" (idempotentă parțial).
    //
    // Ce trebuie să faci:
    // 1. Caută tag-ul: tagRepository.findByName(tagName)
    //    - Returnează Optional<Tag>
    //
    // 2. Dacă tag-ul EXISTĂ → folosește-l direct
    //    Dacă NU EXISTĂ → creează un Tag nou:
    //      a) Tag newTag = new Tag();
    //      b) newTag.setName(tagName);
    //      c) tagRepository.save(newTag) — salvează în DB
    //    Poți folosi .orElseGet(() -> { ... }) pe Optional pentru ambele cazuri.
    //    DE CE orElseGet și nu orElse? — orElseGet primește un Supplier (lambda),
    //    deci codul de creare se execută DOAR dacă tag-ul nu există.
    //
    // 3. Convertește tag-ul în TagResponseDTO.fromEntity(...) și returnează.
    @Transactional
    public TagResponseDTO findOrCreateTag(String tagName) {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

    // ========================================================================================
    // METODA: getAllTags
    // ========================================================================================
    // Scop: Returnează toate tag-urile existente ca o listă de TagResponseDTO-uri.
    //
    // Ce trebuie să faci:
    // 1. Creează o listă goală: List<Tag> tags = new ArrayList<>()
    // 2. tagRepository.findAll().forEach(tags::add)
    //    DE CE forEach cu method reference? — findAll() returnează Iterable, nu List.
    //    forEach iterează prin toate elementele și le adaugă în lista noastră.
    // 3. Convertește în DTO-uri: tags.stream().map(TagResponseDTO::fromEntity).collect(Collectors.toList())
    @Transactional(readOnly = true)
    public List<TagResponseDTO> getAllTags() {
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }

}
