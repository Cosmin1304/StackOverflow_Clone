package com.utcn.demo.service;

import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.utcn.demo.dto.TopicDTO;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public TopicDTO createTopic(Topic topic) {
        topic.setCreatedAt(LocalDateTime.now());
        topic.setStatus("RECEIVED");

        if (topic.getTags() != null) {
            Set<Tag> managedTags = topic.getTags().stream()
                    .map(tag -> tagRepository.findByName(tag.getName())
                            .orElseGet(() -> tagRepository.save(tag)))
                    .collect(Collectors.toSet());
            topic.setTags(managedTags);
        }
        return TopicDTO.fromEntity(topicRepository.save(topic));
    }

    @Transactional(readOnly = true)
    public List<TopicDTO> getAllTopicsSorted() {
        return topicRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(TopicDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopicDTO> getTopicsByTag(String tagName) {
        return topicRepository.findByTags_Name(tagName).stream()
                .map(TopicDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopicDTO> searchByTitle(String title) {
        return topicRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(TopicDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopicDTO> getTopicsByAuthor(User author) {
        return topicRepository.findByAuthor(author).stream()
                .map(TopicDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteTopic(Long id, Long currentUserId) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("topic not found"));

        if (!currentUserId.equals(topic.getAuthor().getId())) {
            throw new RuntimeException("Only the author can delete this topic");
        }
        topicRepository.delete(topic);
    }

    @Transactional
    public TopicDTO updateTopic(Long topicId, String newTitle, String newTextContent, Long currentUserId) {
        Topic topic = topicRepository.findById(topicId)
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

        return TopicDTO.fromEntity(topicRepository.save(topic));
    }
}
