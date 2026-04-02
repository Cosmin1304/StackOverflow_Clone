package com.utcn.demo.repository;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByTopicOrderByCreatedAtDesc(Topic topic);
}
