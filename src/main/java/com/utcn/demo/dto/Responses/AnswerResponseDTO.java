package com.utcn.demo.dto.Responses;

import java.time.LocalDateTime;
import java.util.List;

public record AnswerResponseDTO(
        Long id,
        String text,
        String pictureUrl,
        LocalDateTime createdAt,
        UserResponseDTO author,
        Long topicId,
        List<AnswerVoteResponseDTO> votes
) {}
