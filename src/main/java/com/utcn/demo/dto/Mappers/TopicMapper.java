package com.utcn.demo.dto.Mappers;

import com.utcn.demo.dto.Requests.TopicRequestDTO;
import com.utcn.demo.dto.Responses.AnswerResponseDTO;
import com.utcn.demo.dto.Responses.TagResponseDTO;
import com.utcn.demo.dto.Responses.TopicResponseDTO;
import com.utcn.demo.dto.Responses.TopicVoteResponseDTO;
import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.TopicVote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TopicMapper {

    private final TopicVoteMapper topicVoteMapper;
    private final TagMapper tagMapper;
    private final AnswerMapper answerMapper;
    private final UserMapper userMapper;

    public TopicResponseDTO toResponse(Topic topic) {
        if (topic == null) return null;
        List<TagResponseDTO> tags = mapTags(topic.getTags());
        List<AnswerResponseDTO> answers = mapAnswers(topic.getAnswers());
        List<TopicVoteResponseDTO> votes = mapTopicVotes(topic.getVotes());
        return new TopicResponseDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getTextContent(),
                topic.getPictureUrl(),
                topic.getStatus(),
                topic.getCreatedAt(),
                userMapper.toResponse(topic.getAuthor()),
                tags,
                answers,
                votes
        );
    }

    private List<TopicVoteResponseDTO> mapTopicVotes(Set<TopicVote> topicVotes) {
        return Optional.ofNullable(topicVotes).orElse(Set.of()).stream()
                .map(topicVoteMapper::toResponse).toList();
    }

    private List<AnswerResponseDTO> mapAnswers(List<Answer> answers) {
        return Optional.ofNullable(answers).orElse(List.of()).stream()
                .filter(Objects::nonNull).map(answerMapper::toResponse).toList();
    }

    private List<TagResponseDTO> mapTags(Set<Tag> tags) {
        return Optional.ofNullable(tags).orElse(Set.of()).stream()
                .filter(Objects::nonNull).map(tagMapper::toResponse).toList();
    }

    public Topic toEntity(TopicRequestDTO dto) {
        Topic topic = new Topic();
        topic.setTitle(dto.title());
        topic.setTextContent(dto.text());
        topic.setPictureUrl(dto.pictureUrl());
        topic.setStatus("RECEIVED");
        topic.setCreatedAt(LocalDateTime.now());
        return topic;
    }
}
