package com.utcn.demo.repository;

import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByOrderByCreationDateTimeDesc();

    List<Question> findByTitleContainingIgnoreCase(String title);

    List<Question> findByUser(User user);

    List<Question> findByTags_Name(String tagName);
}
