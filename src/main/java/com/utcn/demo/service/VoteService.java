package com.utcn.demo.service;

import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.TopicVote;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TopicVoteRepository;
import com.utcn.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// VoteService — conține logica de business pentru voturile pe topic-uri.
// Sistemul de vot permite utilizatorilor să dea UPVOTE sau DOWNVOTE pe topic-uri.
// Voturile afectează scorul (reputația) autorului topic-ului.
@Service
public class VoteService {

    @Autowired
    private TopicVoteRepository topicVoteRepository;

    @Autowired
    private UserRepository userRepository;

    // ========================================================================================
    // METODA: voteTopic
    // ========================================================================================
    // Scop: Permite unui utilizator să voteze (UPVOTE/DOWNVOTE) un topic.
    //       Votul afectează scorul autorului topic-ului.
    //
    // Parametri:
    //   - topic: entitatea Topic pe care se votează
    //   - voter: entitatea User care votează
    //   - voteType: String, fie "UPVOTE" fie "DOWNVOTE"
    //
    // Ce trebuie să faci:
    // 1. VALIDARE — Verifică dacă voter-ul este chiar autorul topic-ului:
    //    Dacă voter.getId().equals(topic.getAuthor().getId()) → aruncă RuntimeException
    //    "You cannot vote on your own topic"
    //    DE CE: Nu poți să-ți dai singur upvote — ar fi abuz.
    //
    // 2. Creează un obiect TopicVote nou:
    //    TopicVote vote = new TopicVote();
    //    vote.setUser(voter);    — cine a votat
    //    vote.setTopic(topic);   — pe ce topic
    //    vote.setVoteType(voteType); — ce tip de vot
    //    TopicVote folosește o cheie compusă (TopicVoteKey) cu topic_id + user_id.
    //    @MapsId setează automat valorile din cheie pe baza entităților setate.
    //
    // 3. Actualizează scorul autorului:
    //    User author = topic.getAuthor();
    //    - Dacă voteType == "UPVOTE" → adaugă 2.5 la scor:
    //      author.setScore(author.getScore().add(new BigDecimal("2.5")))
    //    - Dacă voteType == "DOWNVOTE" → scade 1.5 din scor:
    //      author.setScore(author.getScore().subtract(new BigDecimal("1.5")))
    //    DE CE BigDecimal și nu double? — BigDecimal e precis pentru calcule financiare/de scor.
    //    double poate avea erori de precizie (ex: 0.1 + 0.2 != 0.3).
    //
    // 4. Salvează votul: topicVoteRepository.save(vote)
    // 5. Salvează autorul actualizat: userRepository.save(author)
    //    DE CE salvăm și autorul? — I-am modificat scorul, trebuie persistat în DB.
    @Transactional
    public void voteTopic(Topic topic, User voter, String voteType){
        // TODO: Implementează conform pașilor de mai sus
        throw new UnsupportedOperationException("De implementat");
    }
}
