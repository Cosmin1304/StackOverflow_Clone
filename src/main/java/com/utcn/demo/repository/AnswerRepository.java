package com.utcn.demo.repository;

import com.utcn.demo.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByTopic_Id(Long topicId);
}
