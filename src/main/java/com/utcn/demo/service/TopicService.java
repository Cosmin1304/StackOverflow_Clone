package com.utcn.demo.service;

import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TagRepository;
import com.utcn.demo.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TagRepository tagRepository;

    public Topic createTopic(Topic topic) {
        topic.setCreatedAt(LocalDateTime.now());
        topic.setStatus("RECEIVED");

        if (topic.getTags() != null) {
            Set<Tag> managedTags = topic.getTags().stream()
                    .map(tag -> tagRepository.findByName(tag.getName())
                            .orElseGet(() -> tagRepository.save(tag)))
                    .collect(Collectors.toSet());
            topic.setTags(managedTags);

        

    p

        return topicRepository.findAllByOrerByCreatedAtDesc();
    }

    public List<Topic> getTopicsByTag(String tagName) {
        return topicRepository.findByTags_Name(tagName);
    }

    public List<Topic> searchByTitle(String title) {
        return topicRepository.findByTitleContainingIgnoreCase(title);
    }


            throw new RuntimeException("Only the author can delete this topic");

    
    
        
    iTopic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        if (!currentUserId.equals(topic.getAuthor().getId())) {
            throw new RuntimeException("Only the author can edit this topic");
        }

        if (newTitle != null && !newTitle.trim().isEmpty()) {
            topic.setTitle(newTitle);
        }
        if (newTextContent != null && !newTextContent.trim().isEmpty()) {
            topic.setTextContent(newTextContent);
        }

        return topicRepository.save(topic);
    }
}
