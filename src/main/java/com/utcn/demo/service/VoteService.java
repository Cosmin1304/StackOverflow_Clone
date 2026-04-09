package com.utcn.demo.service;

import com.utcn.demo.entity.Topic;
import com.utcn.demo.entity.TopicVote;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TopicRepository;
import com.utcn.demo.repository.TopicVoteRepository;
import com.utcn.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class VoteService {
    @Autowired
    private TopicVoteRepository topicVoteRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void voteTopic(Topic topic, User voter, String voteType){
        if (voter.getId().equals(topic.getAuthor().getId())) {
            throw new RuntimeException("You cannot vote on your own topic");
        }

        TopicVote vote = new TopicVote();
        vote.setUser(voter);
        vote.setTopic(topic);
        vote.setVoteType(voteType);

        User author = topic.getAuthor();
        if ("UPVOTE".equals(voteType)) {
            author.setScore(author.getScore().add(new BigDecimal("2.5")));
        } else if ("DOWNVOTE".equals(voteType)) {
            author.setScore(author.getScore().subtract(new BigDecimal("1.5")));
        }

        topicVoteRepository.save(vote);
        userRepository.save(author);
    }
}
