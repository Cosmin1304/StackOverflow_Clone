package com.utcn.demo.dto.Mappers;

import com.utcn.demo.dto.Responses.AnswerVoteResponseDTO;
import com.utcn.demo.entity.AnswerVote;
import org.springframework.stereotype.Component;

@Component
public class AnswerVoteMapper {
    public AnswerVoteResponseDTO toResponse(AnswerVote vote) {
        if (vote == null) return null;
        Long userId = vote.getUser() != null ? vote.getUser().getId() : null;
        return new AnswerVoteResponseDTO(userId, vote.getVoteType());
    }
}
