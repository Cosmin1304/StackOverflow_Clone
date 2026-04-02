package com.utcn.demo.repository;

import com.utcn.demo.entity.TopicVote;
import com.utcn.demo.entity.TopicVoteKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicVoteRepository extends CrudRepository<TopicVote, TopicVoteKey> {
}
