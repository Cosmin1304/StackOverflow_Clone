package com.utcn.demo.dto.Mappers;

import com.utcn.demo.dto.Requests.AnswerRequestDTO;
import com.utcn.demo.dto.Responses.AnswerResponseDTO;
import com.utcn.demo.dto.Responses.AnswerVoteResponseDTO;
import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.AnswerVote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AnswerMapper {

    private final AnswerVoteMapper answerVoteMapper;
    private final UserMapper userMapper;

    public AnswerResponseDTO toResponse(Answer answer) {
        if (answer == null) return null;
        Long topicId = answer.getTopic() != null ? answer.getTopic().getId() : null;
        List<AnswerVoteResponseDTO> votes = mapAnswerVotes(answer.getVotes());
        return new AnswerResponseDTO(
                answer.getId(),
                answer.getTextContent(),
                answer.getPictureUrl(),
                answer.getCreatedAt(),
                userMapper.toResponse(answer.getAuthor()),
                topicId,
                votes
        );
    }

    private List<AnswerVoteResponseDTO> mapAnswerVotes(Set<AnswerVote> votes) {
        return Optional.ofNullable(votes).orElse(Set.of()).stream()
                .map(answerVoteMapper::toResponse).toList();
    }

    public Answer toEntity(AnswerRequestDTO dto) {
        Answer answer = new Answer();
        answer.setTextContent(dto.answerText());
        answer.setPictureUrl(dto.pictureURL());
        answer.setCreatedAt(LocalDateTime.now());
        return answer;
    }
}
