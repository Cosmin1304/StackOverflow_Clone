package com.utcn.demo.repository;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    List<Answer> findByTopicOrderByCreatedAtDesc(Topic topic);
}
