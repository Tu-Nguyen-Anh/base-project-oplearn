package org.oplearn.project.service;

import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.topic.FollowedTopicResponse;

import java.util.List;

public interface UserFollowTopicService {
    void followTopic(Long userId, Long topicId);

    void unfollowTopic(Long userId, Long topicId);

    PageResponse<FollowedTopicResponse> getFollowedTopics(Long userId, int page, int size);

    List<Long> getFollowedTopicIds(Long userId);
}
