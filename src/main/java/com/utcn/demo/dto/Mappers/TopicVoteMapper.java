package com.utcn.demo.dto.Mappers;

import com.utcn.demo.dto.Responses.TopicVoteResponseDTO;
import com.utcn.demo.entity.TopicVote;
import org.springframework.stereotype.Component;

@Component
public class TopicVoteMapper {
    public TopicVoteResponseDTO toResponse(TopicVote topicVote) {
        if (topicVote == null) return null;
        Long userId = topicVote.getUser() != null ? topicVote.getUser().getId() : null;
        return new TopicVoteResponseDTO(userId, topicVote.getVoteType());
    }
}
