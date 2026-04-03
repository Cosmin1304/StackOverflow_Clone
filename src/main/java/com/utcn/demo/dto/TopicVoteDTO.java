package com.utcn.demo.dto;

import com.utcn.demo.entity.TopicVote;

public record TopicVoteDTO(Long userId, String voteType) {
    public static TopicVoteDTO fromEntity(TopicVote vote) {
        if (vote == null)
            return null;
        return new TopicVoteDTO(vote.getUser() != null ? vote.getUser().getId() : null, vote.getVoteType());
    }
}
