package com.utcn.demo.service;

import com.utcn.demo.dto.Mappers.TopicMapper;
import com.utcn.demo.dto.Requests.TopicRequestDTO;
import com.utcn.demo.dto.Requests.UserRequestDTO;
import com.utcn.demo.dto.Responses.TopicResponseDTO;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TagRepository;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

// @Service — marchează clasa ca un Service Spring (bean detectat automat).
// TopicService conține logica de business pentru Topic-uri (întrebări/discuții pe forum).
@Service
@RequiredArgsConstructor
public class  TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final TopicMapper topicMapper;

    @Transactional
    public TopicResponseDTO createTopic(TopicRequestDTO topic) {
        if(topic ==  null) throw new IllegalArgumentException("topic is null");
        Topic topicEntity = topicMapper.toEntity(topic);
        return topicMapper.toResponse(topicRepository.save(topicEntity));
    }


    @Transactional(readOnly = true)
    public List<TopicResponseDTO> getAllTopicsSorted() {
        List<Topic>topics = topicRepository.findAllByOrderByCreatedAtDesc();
        return Optional.ofNullable(topics).stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(topicMapper::toResponse)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (list.isEmpty()) throw new RuntimeException("No topics found");
                            return list;
                        }
                ));

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
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        (list) -> {
                            if (list.isEmpty()) throw new RuntimeException("No topics found");
                            return list;
                        }
                ));


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
                .collect(Collectors.collectingAndThen(
                        Collectors.<R>toList(),
                        (list) -> {
                            if (list.isEmpty()) throw new RuntimeException("No topics found");
                            return list;
                        }
                ));
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
        if(!currentUserId.equals(topic.getAuthor().getId())) throw new RuntimeException("user not authorized");
        topicRepository.delete(topic);
    }

    @Transactional
    public TopicResponseDTO updateTopic(Long topicId, TopicRequestDTO updates, Long currentUserId) {
        return topicRepository.findById(topicId)
                .map(existingTopic -> {
                    if (!Objects.equals(existingTopic.getAuthor().getId(), currentUserId)) {
                        throw new RuntimeException("user not authorized");
                    }
                    return applyUpdates(existingTopic, updates);
                })
                .map(topicRepository::save)
                .map(topicMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("topic not found"));
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
