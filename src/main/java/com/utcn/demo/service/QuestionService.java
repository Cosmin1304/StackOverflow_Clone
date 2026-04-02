package com.utcn.demo.service;

import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.Tag;
import com.utcn.demo.repository.QuestionRepository;
import com.utcn.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;

    public Question askQuestion(Question question) {
        question.setCreationDateTime(LocalDateTime.now());
        question.setStatus(Question.QuestionStatus.RECEIVED);
        List<Tag> managedTags = question.getTags().stream()
                .map(tag -> tagRepository.findByName(tag.getName())
                        .orElseGet(() -> tagRepository.save(tag))).toList();

        question.setTags(managedTags);

        return questionRepository.save(question);
    }

    public List<Question> getAllQuestionsSorted() {
        return questionRepository.findAllByOrderByCreationDateTimeDesc();
    }

    public List<Question> getQuestionsByTag(String tagName) {
        return questionRepository.findByTags_Name(tagName);
    }

    public List<Question> searchByTitle(String title) {
        return questionRepository.findByTitleContainingIgnoreCase(title);
    }

    public void deleteQuestion(Long id, Long currentUserId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getUser().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Only the author can delete this question!");
        }

        questionRepository.delete(question);
    }
}
