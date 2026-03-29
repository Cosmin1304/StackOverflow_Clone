package com.utcn.demo.repository;

import com.utcn.demo.entity.AnswerVote;
import com.utcn.demo.entity.AnswerVoteKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerVoteRepository extends JpaRepository<AnswerVote, AnswerVoteKey> {
}