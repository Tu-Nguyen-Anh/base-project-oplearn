package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.TopicFilterRequest;
import org.oplearn.project.dto.request.TopicRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.topic.FollowedTopicResponse;
import org.oplearn.project.dto.response.topic.TopicFilterResponse;
import org.oplearn.project.dto.response.topic.TopicResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.facade.TopicFacadeService;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.TopicService;
import org.oplearn.project.service.UserFollowTopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicFacadeServiceImpl implements TopicFacadeService {
    private final TopicService topicService;
    private final UserFollowTopicService userFollowTopicService;

    @Transactional
    @Override
    public TopicResponse create(TopicRequest request) {
        log.info("=== Start create");
        log.debug("(create) request: {}", request);

        return topicService.create(request);
    }

    @Transactional
    @Override
    public TopicResponse update(TopicRequest request, Long id) {
        log.info("=== Start update");
        log.debug("(update) request: {}", request);

        return topicService.update(request, id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("=== Start delete");
        log.debug("(delete) id: {}", id);
        topicService.delete(id);
    }

    @Override
    public PageResponse<TopicFilterResponse> filter(TopicFilterRequest request) {
        log.info("=== Start filter");
        log.debug("(filter) request: {}", request);

        return topicService.filter(request);
    }

    @Transactional
    @Override
    public void followTopic(Long topicId) {
        log.info("=== Start followTopic");
        log.debug("(followTopic) topicId: {}", topicId);

        topicService.checkExistById(topicId);
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        userFollowTopicService.followTopic(currentUser.getId(), topicId);
    }

    @Transactional
    @Override
    public void unfollowTopic(Long topicId) {
        log.info("=== Start unfollowTopic");
        log.debug("(unfollowTopic) topicId: {}", topicId);

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        userFollowTopicService.unfollowTopic(currentUser.getId(), topicId);
    }

    @Override
    public PageResponse<FollowedTopicResponse> getFollowedTopics(int page, int size) {
        log.info("=== Start getFollowedTopics");

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return userFollowTopicService.getFollowedTopics(currentUser.getId(), page, size);
    }
}



