package com.utcn.demo.dto;

import com.utcn.demo.entity.Answer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record AnswerDTO(
        Long id,
        String textContent,
        String pictureUrl,
        LocalDateTime createdAt,
        UserDTO author,
        Long topicId,
        List<AnswerVoteDTO> votes) {
    public static AnswerDTO fromEntity(Answer answer) {
        if (answer == null)
            return null;
        Long tId = answer.getTopic() != null ? answer.getTopic().getId() : null;
        return new AnswerDTO(
                answer.getId(),
                answer.getTextContent(),
                answer.getPictureUrl(),
                answer.getCreatedAt(),
                UserDTO.fromEntity(answer.getAuthor()),
                tId,
                answer.getVotes() != null
                        ? answer.getVotes().stream().map(AnswerVoteDTO::fromEntity).collect(Collectors.toList())
                        : List.of());
    }
}
