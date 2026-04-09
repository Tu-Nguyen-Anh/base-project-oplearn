package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.topic.FollowedTopicResponse;
import org.oplearn.project.entity.topic.UserFollowTopic;
import org.oplearn.project.exception.base.topic.FollowTopicNotFoundException;
import org.oplearn.project.exception.base.topic.TopicAlreadyFollowedException;
import org.oplearn.project.repository.UserFollowTopicRepository;
import org.oplearn.project.service.UserFollowTopicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFollowTopicServiceImpl implements UserFollowTopicService {
    private final UserFollowTopicRepository repository;

    @Transactional
    @Override
    public void followTopic(Long userId, Long topicId) {
        log.info("=== Start followTopic");
        log.debug("(followTopic) userId: {}, topicId: {}", userId, topicId);

        if (repository.existsByUserIdAndTopicIdAndDeletedFalse(userId, topicId)) {
            throw new TopicAlreadyFollowedException();
        }

        UserFollowTopic follow = UserFollowTopic.builder()
                .userId(userId)
                .topicId(topicId)
                .deleted(false)
                .build();

        repository.save(follow);
    }

    @Transactional
    @Override
    public void unfollowTopic(Long userId, Long topicId) {
        log.info("=== Start unfollowTopic");
        log.debug("(unfollowTopic) userId: {}, topicId: {}", userId, topicId);

        UserFollowTopic follow = repository.findByUserIdAndTopicId(userId, topicId)
                .orElseThrow(FollowTopicNotFoundException::new);

        follow.setDeleted(true);
        repository.save(follow);
    }

    @Override
    public PageResponse<FollowedTopicResponse> getFollowedTopics(Long userId, int page, int size) {
        log.info("=== Start getFollowedTopics");
        log.debug("(getFollowedTopics) userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<FollowedTopicResponse> result = repository.findFollowedTopicsByUserId(userId, pageable);

        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }

    @Override
    public List<Long> getFollowedTopicIds(Long userId) {
        log.info("=== Start getFollowedTopicIds");
        log.debug("(getFollowedTopicIds) userId: {}", userId);

        return repository.findFollowedTopicIdsByUserId(userId);
    }
}
