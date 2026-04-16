package com.utcn.demo.repository;

import com.utcn.demo.entity.TopicVote;
import com.utcn.demo.entity.TopicVoteKey;
import com.utcn.demo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicVoteRepository extends CrudRepository<TopicVote, TopicVoteKey> {

    Optional<TopicVote> findById(TopicVoteKey id);
}
