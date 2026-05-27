package com.utcn.demo.repository;

import com.utcn.demo.entity.AnswerVote;
import com.utcn.demo.entity.AnswerVoteKey;
import com.utcn.demo.entity.TopicVote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerVoteRepository extends CrudRepository<AnswerVote, AnswerVoteKey> {

    Optional<AnswerVote> findByAnswerIdAndUserId(Long answerId, Long userId);

}