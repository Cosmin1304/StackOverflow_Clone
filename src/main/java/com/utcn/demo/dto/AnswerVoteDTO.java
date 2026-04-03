package com.utcn.demo.dto;

import com.utcn.demo.entity.AnswerVote;

public record AnswerVoteDTO(Long userId, String voteType) {
    public static AnswerVoteDTO fromEntity(AnswerVote vote) {
        if (vote == null)
            return null;
        return new AnswerVoteDTO(vote.getUser() != null ? vote.getUser().getId() : null, vote.getVoteType());
    }
}
