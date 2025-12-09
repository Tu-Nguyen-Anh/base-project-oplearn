package org.oplearn.project.service;

import org.oplearn.project.dto.request.TopicFilterRequest;
import org.oplearn.project.dto.request.TopicRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.topic.TopicFilterResponse;
import org.oplearn.project.dto.response.topic.TopicResponse;
import org.oplearn.project.entity.topic.Topic;

public interface TopicService {
    Topic getById(Long topicId);

    TopicResponse create(TopicRequest request);

    TopicResponse update(TopicRequest request, Long id);

    void delete(Long id);

    void checkNameExists(String name);

    void checkUrlExists(String url);

    void save(Topic topic);

    PageResponse<TopicFilterResponse> filter(TopicFilterRequest request);

    TopicResponse detail(Long id);

    Topic checkExistById(Long id);

    void deleteBySourceId(Long sourceId);
}



