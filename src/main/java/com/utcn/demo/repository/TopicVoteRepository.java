package com.utcn.demo.repository;

import com.utcn.demo.entity.TopicVote;
import com.utcn.demo.entity.TopicVoteKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicVoteRepository extends JpaRepository<TopicVote, TopicVoteKey> {
}
