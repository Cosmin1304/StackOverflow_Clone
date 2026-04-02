package com.utcn.demo.repository;

import com.utcn.demo.entity.AnswerVote;
import com.utcn.demo.entity.AnswerVoteKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerVoteRepository extends CrudRepository<AnswerVote, AnswerVoteKey> {
}