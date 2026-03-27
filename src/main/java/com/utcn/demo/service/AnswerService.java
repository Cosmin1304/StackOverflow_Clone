package com.utcn.demo.service;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Question;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Transactional
    public Answer addAnswer (Long questionId, Answer answer) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()->new RuntimeException("Question not found"));

        if (question.getStatus() == Question.QuestionStatus.SOLVED) {
            throw new RuntimeException("This question is already solved, no more answers can be added");
        }

        answer.setCreationDateTime(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setLikeCount(0);

        if (question.getAnswers().isEmpty() && question.getStatus().equals(Question.QuestionStatus.RECEIVED)) {
            question.setStatus(Question.QuestionStatus.IN_PROGRESS);
            questionRepository.save(question);
        }

        return answerRepository.save(answer);

    }

    public List<Answer> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestion_QuestionIdOrderByLikeCountDesc(questionId);
    }

    public Answer updateAnswer(Long answerId, String newText, Long currentUserId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        if (!answer.getUser().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Only the author can edit this answer!");
        }
        answer.setText(newText);
        return answerRepository.save(answer);
    }

    public void deleteAnswer(Long answerId, Long currentUserId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));

        if (!answer.getUser().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Only the author can delete this answer!");
        }

        answerRepository.delete(answer);
    }
}
