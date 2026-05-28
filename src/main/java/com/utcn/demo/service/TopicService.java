package com.utcn.demo.service;

import com.utcn.demo.dto.Mappers.TopicMapper;
import com.utcn.demo.dto.Requests.TopicRequestDTO;
import com.utcn.demo.dto.Requests.UserRequestDTO;
import com.utcn.demo.dto.Responses.TopicResponseDTO;
import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TagRepository;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class  TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final TopicMapper topicMapper;
    private final TagRepository tagRepository;

    @Transactional
    public TopicResponseDTO createTopic(TopicRequestDTO topic, User author) {
        if(topic ==  null) throw new IllegalArgumentException("topic is null");
        Topic topicEntity = topicMapper.toEntity(topic);
        topicEntity.setAuthor(author);

        if (topic.tagNames() != null && !topic.tagNames().isEmpty()) {
            Set<Tag> managedTags = topic.tagNames().stream()
                    .map(tagName -> {
                        return tagRepository.findByName(tagName)
                                .orElseGet(() -> {
                                    com.utcn.demo.entity.Tag newTag = new com.utcn.demo.entity.Tag();
                                    newTag.setName(tagName);
                                    return tagRepository.save(newTag);
                                });
                    })
                    .collect(Collectors.toSet());
            topicEntity.setTags(managedTags);
        }

        return topicMapper.toResponse(topicRepository.save(topicEntity));
    }


    @Transactional(readOnly = true)
    public List<TopicResponseDTO> getAllTopicsSorted() {
        List<Topic>topics = topicRepository.findAllByOrderByCreatedAtDesc();
        return Optional.ofNullable(topics).stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(topicMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<TopicResponseDTO> getTopicsByTag(String tagName) {
        List<Topic> topics = topicRepository.findByTags_Name(tagName);
        Function<? extends  Topic,? super TopicResponseDTO> mapper = t -> topicMapper.toResponse(t);
        return mapOrThrow(topics, topicMapper::toResponse);
    }
    private Function<List<Topic> , List<TopicResponseDTO>> getTopicExecutor(){

        return topics -> Optional.ofNullable(topics)
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(topicMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TopicResponseDTO getTopicById(Long id) {
        return topicRepository.findById(id)
                .map(topicMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
    }

    public List<TopicResponseDTO> execute(List<Topic> topics) {
        return getTopicExecutor().apply(topics);
    }

    private <T , R> List<R> mapOrThrow(List<? extends T> items, Function<? super T , ? extends R> mapper){
        return Optional.ofNullable(items)
                .stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopicResponseDTO> searchByTitle(String title) {
        List<Topic> matchingTopics = topicRepository.findByTitleContainingIgnoreCase(title);
        return mapOrThrow(matchingTopics, topicMapper::toResponse);
    }


    @Transactional(readOnly = true)
    public List<TopicResponseDTO> getTopicsByAuthor(UserRequestDTO authorDTO) {
        Optional<User> author = userRepository.findByUsername(authorDTO.username());
        author.orElseThrow(() -> new RuntimeException("username not found"));
        List<Topic> matchingTopics = topicRepository.findByAuthor(author.get());
        return mapOrThrow(matchingTopics, topicMapper::toResponse);
    }


    public void deleteTopic(Long id, Long currentUserId) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("topic not found"));
        if(!isAuthorOrModerator(topic.getAuthor().getId(), currentUserId)) throw new RuntimeException("user not authorized");
        topicRepository.delete(topic);
    }

    @Transactional
    public TopicResponseDTO updateTopic(Long topicId, TopicRequestDTO updates, Long currentUserId) {
        return topicRepository.findById(topicId)
                .map(existingTopic -> {
                    if (!isAuthorOrModerator(existingTopic.getAuthor().getId(), currentUserId)) {
                        throw new RuntimeException("user not authorized");
                    }
                    return applyUpdates(existingTopic, updates);
                })
                .map(topicRepository::save)
                .map(topicMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("topic not found"));
    }

    private boolean isAuthorOrModerator(Long authorId, Long currentUserId) {
        if (Objects.equals(authorId, currentUserId)) {
            return true;
        }
        return userRepository.findById(currentUserId)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> "MODERATOR".equals(role.getRoleName())))
                .orElse(false);
    }

    private Topic applyUpdates(Topic existingTopic, TopicRequestDTO dto) {
        if (dto.title() != null && !dto.title().trim().isEmpty()) {
            existingTopic.setTitle(dto.title());
        }
        if (dto.text() != null && !dto.text().trim().isEmpty()) {
            existingTopic.setTextContent(dto.text());
        }
        if (dto.pictureUrl() != null && !dto.pictureUrl().trim().isEmpty()) {
            existingTopic.setPictureUrl(dto.pictureUrl());
        }

        if (dto.tagNames() != null) {
            java.util.Set<com.utcn.demo.entity.Tag> managedTags = dto.tagNames().stream()
                    .map(tagName -> {
                        return tagRepository.findByName(tagName)
                                .orElseGet(() -> {
                                    com.utcn.demo.entity.Tag newTag = new com.utcn.demo.entity.Tag();
                                    newTag.setName(tagName);
                                    return tagRepository.save(newTag);
                                });
                    })
                    .collect(java.util.stream.Collectors.toSet());

            existingTopic.setTags(managedTags);
        }

        return existingTopic;
    }

    @Transactional
    public TopicResponseDTO acceptAnswer(Long topicId, Long currentUserId) {
        return topicRepository.findById(topicId)
                .map(topic -> {
                    if (!Objects.equals(topic.getAuthor().getId(), currentUserId)) {
                        throw new RuntimeException("user not authorized");
                    }
                    topic.setStatus("SOLVED");
                    return topic;
                })
                .map(topicRepository::save)
                .map(topicMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
    }

}
