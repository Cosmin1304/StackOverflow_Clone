package com.utcn.demo.dto.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record TopicResponseDTO(
        Long id,
        String title,
        @JsonProperty("text")
        String textContent,
        String pictureUrl,
        String status,
        LocalDateTime createdAt,
        UserResponseDTO author,
        List<TagResponseDTO> tags,
        List<AnswerResponseDTO> answers,
        List<TopicVoteResponseDTO> votes
) {}
