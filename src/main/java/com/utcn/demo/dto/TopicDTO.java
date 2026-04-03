package com.utcn.demo.dto;

import com.utcn.demo.entity.Topic;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record TopicDTO(
        Long id,
        String title,
        String textContent,
        String pictureUrl,
        String status,
        LocalDateTime createdAt,
        UserDTO author,
        List<TagDTO> tags,
        List<AnswerDTO> answers,
        List<TopicVoteDTO> votes) {
    public static TopicDTO fromEntity(Topic topic) {
        if (topic == null)
            return null;
        return new TopicDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getTextContent(),
                topic.getPictureUrl(),
                topic.getStatus(),
                topic.getCreatedAt(),
                UserDTO.fromEntity(topic.getAuthor()),
                topic.getTags() != null ? topic.getTags().stream().map(TagDTO::fromEntity).collect(Collectors.toList())
                        : List.of(),
                topic.getAnswers() != null
                        ? topic.getAnswers().stream().map(AnswerDTO::fromEntity).collect(Collectors.toList())
                        : List.of(),
                topic.getVotes() != null
                        ? topic.getVotes().stream().map(TopicVoteDTO::fromEntity).collect(Collectors.toList())
                        : List.of());
    }
}
