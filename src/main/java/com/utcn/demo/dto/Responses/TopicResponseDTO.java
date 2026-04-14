package com.utcn.demo.dto.Responses;

import java.time.LocalDateTime;
import java.util.List;

public record TopicResponseDTO(
        Long id,
        String title,
        String textContent,
        String pictureUrl,
        String status,
        LocalDateTime createdAt,
        UserResponseDTO author,
        List<TagResponseDTO> tags,
        List<AnswerResponseDTO> answers,
        List<TopicVoteResponseDTO> votes
) {}
