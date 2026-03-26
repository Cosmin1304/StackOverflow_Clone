package com.utcn.demo.repository;

import com.utcn.demo.entity.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<Topic,Long> {
}
