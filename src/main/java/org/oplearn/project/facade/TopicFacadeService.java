package org.oplearn.project.facade;

import org.oplearn.project.dto.request.TopicFilterRequest;
import org.oplearn.project.dto.request.TopicRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.topic.FollowedTopicResponse;
import org.oplearn.project.dto.response.topic.TopicFilterResponse;
import org.oplearn.project.dto.response.topic.TopicResponse;

public interface TopicFacadeService {
    TopicResponse create(TopicRequest request);

    TopicResponse update(TopicRequest request, Long id);

    void delete(Long id);

    PageResponse<TopicFilterResponse> filter(TopicFilterRequest request);

    void followTopic(Long topicId);

    void unfollowTopic(Long topicId);

    PageResponse<FollowedTopicResponse> getFollowedTopics(int page, int size);
}



