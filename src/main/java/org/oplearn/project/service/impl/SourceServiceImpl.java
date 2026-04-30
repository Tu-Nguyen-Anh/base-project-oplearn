package org.oplearn.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.SourceFilterRequest;
import org.oplearn.project.dto.request.SourceRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.source.SourceFilterResponse;
import org.oplearn.project.dto.response.source.SourceResponse;
import org.oplearn.project.dto.response.source.SourceWithTopicsResponse;
import org.oplearn.project.dto.response.topic.TopicSimpleResponse;
import org.oplearn.project.entity.source.Source;
import org.oplearn.project.entity.topic.Topic;
import org.oplearn.project.exception.base.SourceNotFoundException;
import org.oplearn.project.exception.base.source.NameAlreadyExistedException;
import org.oplearn.project.exception.base.source.UrlAlreadyExistedException;
import org.oplearn.project.repository.SourceRepository;
import org.oplearn.project.repository.TopicRepository;
import org.oplearn.project.repository.UserFollowTopicRepository;
import org.oplearn.project.service.SourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SourceServiceImpl implements SourceService {
    private final SourceRepository repository;
    private final TopicRepository topicRepository;
    private final UserFollowTopicRepository userFollowTopicRepository;

    public SourceServiceImpl(SourceRepository repository, TopicRepository topicRepository,
                              UserFollowTopicRepository userFollowTopicRepository) {
        this.repository = repository;
        this.topicRepository = topicRepository;
        this.userFollowTopicRepository = userFollowTopicRepository;
    }

    @Override
    public Source getById(Long sourceId) {
        return repository.getByIdAndDeletedFalse(sourceId);
    }

    @Transactional
    @Override
    public SourceResponse create(SourceRequest request) {
        log.info("create");
        log.debug("Request: {}", request);

        Source source = this.mapRequestToEntity(request);

        source = repository.save(source);

        return this.mapEntityToResponse(source);
    }

    private Source mapRequestToEntity(SourceRequest sourceRequest) {
        return new Source(
                sourceRequest.getName(),
                sourceRequest.getUrl(),
                sourceRequest.getAvatar(),
                sourceRequest.getType(),
                sourceRequest.getDescription()
        );
    }

    private SourceResponse mapEntityToResponse(Source source) {
        return new SourceResponse(
                source.getId(),
                source.getName(),
                source.getUrl(),
                source.getAvatar(),
                source.getType(),
                source.getDescription(),
                source.getActive()
        );
    }

    @Transactional
    @Override
    public SourceResponse update(SourceRequest request, Long id) {
        log.info("update");
        log.debug("request: {}, id: {}", request, id);

        Source existingSource = this.checkExistById(id);
        this.checkValidForUpdate(existingSource, request);
        this.setValueForUpdate(existingSource, request);

        existingSource = repository.save(existingSource);
        return this.mapEntityToResponse(existingSource);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Deleting");
        log.debug("id: {}", id);

        Source source = this.checkExistById(id);
        source.setDeleted(true);
        repository.save(source);
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
    public void save(Source source) {
        repository.save(source);
    }

    @Override
    public PageResponse<SourceFilterResponse> filter(SourceFilterRequest request) {
        log.info("Filtering");
        log.debug("Request: {}", request);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        String keyword = request.getKeyword() == null ? "" : request.getKeyword();
        Page<SourceFilterResponse> sources = repository.filter(
                keyword,
                request.getType(),
                request.getActive(),
                pageable
        );

        return PageResponse.of(sources.getContent(), (int) sources.getTotalElements());
    }

    @Override
    public SourceResponse detail(Long id) {
        log.info("=== Start detail");
        log.debug("id: {}", id);
        Source source = repository.getByIdAndDeletedFalse(id);
        if (Objects.isNull(source) || source.getDeleted() == true) {
            log.error("Source not found exception");
            throw new SourceNotFoundException();
        }
        return mapEntityToResponse(source);
    }

    @Override
    public Source checkExistById(Long id) {
        log.info("(checkExistById");
        log.debug("checkExistById: {}", id);

        Source source = repository.findById(id).orElseThrow(SourceNotFoundException::new);
        if (source == null || source.getDeleted()) {
            throw new SourceNotFoundException();
        }
        return source;
    }

    private void checkValidForUpdate(Source source, SourceRequest request) {
        if (Objects.nonNull(source.getName()) && !source.getName().equals(request.getName()) &&
                isNameExisted(request.getName())) {
            throw new NameAlreadyExistedException();
        }

        if (Objects.nonNull(source.getUrl()) && !source.getUrl().equals(request.getUrl()) &&
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

    private void setValueForUpdate(Source source, SourceRequest request) {
        if (request.getName() != null) {
            source.setName(request.getName());
        }
        if (request.getUrl() != null) {
            source.setUrl(request.getUrl());
        }
        if (request.getAvatar() != null) {
            source.setAvatar(request.getAvatar());
        }
        if (request.getType() != null) {
            source.setType(request.getType());
        }
        if (request.getDescription() != null) {
            source.setDescription(request.getDescription());
        }
    }

    @Override
    public List<SourceWithTopicsResponse> getAllWithTopics(Long userId) {
        log.info("=== Start getAllWithTopics");

        List<Source> sources = repository.findAllNotDeleted();

        Set<Long> followedTopicIds = userId != null
                ? new HashSet<>(userFollowTopicRepository.findFollowedTopicIdsByUserId(userId))
                : new HashSet<>();

        return sources.stream()
                .map(source -> mapToSourceWithTopicsResponse(source, followedTopicIds))
                .collect(Collectors.toList());
    }

    private SourceWithTopicsResponse mapToSourceWithTopicsResponse(Source source, Set<Long> followedTopicIds) {
        List<Topic> topics = topicRepository.findBySourceIdAndDeletedFalse(source.getId());

        List<TopicSimpleResponse> topicResponses = topics.stream()
                .map(topic -> new TopicSimpleResponse(
                        topic.getId(),
                        topic.getName(),
                        topic.getUrl(),
                        topic.getRssUrl(),
                        topic.getDescription(),
                        followedTopicIds.contains(topic.getId())
                ))
                .collect(Collectors.toList());

        return new SourceWithTopicsResponse(
                source.getId(),
                source.getName(),
                source.getUrl(),
                source.getAvatar(),
                source.getType(),
                source.getDescription(),
                topicResponses
        );
    }
}

