package com.utcn.demo.repository;

import com.utcn.demo.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByOrderByCreatedAtDesc();
    List<Topic> findByTags_Name(String name);
    List<Topic> findByTitleContainingIgnoreCase(String title);
}
