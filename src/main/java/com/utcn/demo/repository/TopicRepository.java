package com.utcn.demo.repository;

import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {
    List<Topic> findAllByOrderByCreatedAtDesc();
    List<Topic> findByTitleContainingIgnoreCase(String title);
    List<Topic> findByAuthor(User author);
    List<Topic> findByTags_Name(String tagName);
}


