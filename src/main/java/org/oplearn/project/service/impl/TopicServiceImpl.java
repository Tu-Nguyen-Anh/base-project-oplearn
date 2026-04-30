package org.oplearn.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.TopicFilterRequest;
import org.oplearn.project.dto.request.TopicRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.topic.TopicFilterResponse;
import org.oplearn.project.dto.response.topic.TopicResponse;
import org.oplearn.project.entity.source.Source;
import org.oplearn.project.entity.topic.Topic;
import org.oplearn.project.exception.base.SourceNotFoundException;
import org.oplearn.project.exception.base.TopicNotFoundException;
import org.oplearn.project.exception.base.topic.NameAlreadyExistedException;
import org.oplearn.project.exception.base.topic.UrlAlreadyExistedException;
import org.oplearn.project.repository.SourceRepository;
import org.oplearn.project.repository.TopicRepository;
import org.oplearn.project.service.TopicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository repository;
    private final SourceRepository sourceRepository;

    public TopicServiceImpl(TopicRepository repository, SourceRepository sourceRepository) {
        this.repository = repository;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public Topic getById(Long topicId) {
        return repository.getByIdAndDeletedFalse(topicId);
    }

    @Transactional
    @Override
    public TopicResponse create(TopicRequest request) {
        log.info("create");
        log.debug("Request: {}", request);

        Topic topic = this.mapRequestToEntity(request);

        topic = repository.save(topic);

        return this.mapEntityToResponse(topic);
    }

    private Topic mapRequestToEntity(TopicRequest topicRequest) {
        Source source = sourceRepository.getByIdAndDeletedFalse(topicRequest.getSourceId());
        if (source == null || source.getDeleted()) {
            throw new SourceNotFoundException();
        }

        return new Topic(
                topicRequest.getName(),
                topicRequest.getUrl(),
                topicRequest.getRssUrl(),
                topicRequest.getDescription(),
                source.getId()
        );
    }

    private TopicResponse mapEntityToResponse(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getName(),
                topic.getUrl(),
                topic.getRssUrl(),
                topic.getDescription(),
                topic.getActive()
        );
    }

    @Transactional
    @Override
    public TopicResponse update(TopicRequest request, Long id) {
        log.info("update");
        log.debug("request: {}, id: {}", request, id);

        Topic existingTopic = this.checkExistById(id);
        this.checkValidForUpdate(existingTopic, request);
        this.setValueForUpdate(existingTopic, request);

        existingTopic = repository.save(existingTopic);
        return this.mapEntityToResponse(existingTopic);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Deleting");
        log.debug("id: {}", id);

        Topic topic = this.checkExistById(id);
        topic.setDeleted(true);
        repository.save(topic);
    }

    @Override
    public void checkNameExists(String name) {
        log.info("=== Start checkNameExists");
        log.debug("(checkNameExists) name: {}", name);

        if (isNameExisted(name)) {
            throw new NameAlreadyExistedException();
        }
    }

    @Override
    public void checkUrlExists(String url) {
        log.info("=== Start checkUrlExists");
        log.debug("(checkUrlExists) url: {}", url);

        if (isUrlExisted(url)) {
            throw new UrlAlreadyExistedException();
        }
    }

    @Override
    public void save(Topic topic) {
        repository.save(topic);
    }

    @Override
    public PageResponse<TopicFilterResponse> filter(TopicFilterRequest request) {
        log.info("Filtering");
        log.debug("Request: {}", request);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        String keyword = request.getKeyword() == null ? "" : request.getKeyword();
        Page<TopicFilterResponse> topics = repository.filter(
                keyword,
                request.getSourceId(),
                request.getActive(),
                pageable
        );

        return PageResponse.of(topics.getContent(), (int) topics.getTotalElements());
    }

    @Override
    public TopicResponse detail(Long id) {
        log.info("=== Start detail");
        log.debug("id: {}", id);
        Topic topic = repository.getByIdAndDeletedFalse(id);
        if (Objects.isNull(topic) || topic.getDeleted() == true) {
            log.error("Topic not found exception");
            throw new TopicNotFoundException();
        }
        return mapEntityToResponse(topic);
    }

    @Override
    public Topic checkExistById(Long id) {
        log.info("(checkExistById");
        log.debug("checkExistById: {}", id);

        Topic topic = repository.findById(id).orElseThrow(TopicNotFoundException::new);
        if (topic == null || topic.getDeleted()) {
            throw new TopicNotFoundException();
        }
        return topic;
    }

    @Override
    public void deleteBySourceId(Long sourceId) {
        log.info("=== Start deleteBySourceId: {}", sourceId);

        repository.deleteBySourceId(sourceId);
    }

    private void checkValidForUpdate(Topic topic, TopicRequest request) {
        if (Objects.nonNull(topic.getName()) && !topic.getName().equals(request.getName()) &&
                isNameExisted(request.getName())) {
            throw new NameAlreadyExistedException();
        }

        if (Objects.nonNull(topic.getUrl()) && !topic.getUrl().equals(request.getUrl()) &&
                isUrlExisted(request.getUrl())) {
            throw new UrlAlreadyExistedException();
        }
    }

    private boolean objectNotNullAndNotEmpty(String object) {
        return object != null && !object.isEmpty();
    }

    private boolean isNameExisted(String name) {
        return objectNotNullAndNotEmpty(name)
                && repository.existsByNameAndDeletedIsFalse(name);
    }

    private boolean isUrlExisted(String url) {
        return objectNotNullAndNotEmpty(url)
                && repository.existsByUrlAndDeletedIsFalse(url);
    }

    private void setValueForUpdate(Topic topic, TopicRequest request) {
        if (request.getName() != null) {
            topic.setName(request.getName());
        }
        if (request.getUrl() != null) {
            topic.setUrl(request.getUrl());
        }
        if (request.getRssUrl() != null) {
            topic.setRssUrl(request.getRssUrl());
        }
        if (request.getDescription() != null) {
            topic.setDescription(request.getDescription());
        }
        if (request.getSourceId() != null) {
            topic.setSourceId(request.getSourceId());
        }
    }
}



