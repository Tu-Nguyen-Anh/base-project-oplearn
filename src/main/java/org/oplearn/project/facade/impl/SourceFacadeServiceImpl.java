package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.SourceFilterRequest;
import org.oplearn.project.dto.request.SourceRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.source.SourceFilterResponse;
import org.oplearn.project.dto.response.source.SourceResponse;
import org.oplearn.project.facade.SourceFacadeService;
import org.oplearn.project.service.SourceService;
import org.oplearn.project.service.TopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceFacadeServiceImpl implements SourceFacadeService {
    private final SourceService sourceService;
    private final TopicService  topicService;

    @Transactional
    @Override
    public SourceResponse create(SourceRequest request) {
        log.info("=== Start create");
        log.debug("(create) request: {}", request);

        return sourceService.create(request);
    }

    @Transactional
    @Override
    public SourceResponse update(SourceRequest request, Long id) {
        log.info("=== Start update");
        log.debug("(update) request: {}", request);

        return sourceService.update(request, id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("=== Start delete");
        log.debug("(delete) id: {}", id);

        topicService.deleteBySourceId(id);
        sourceService.delete(id);
    }

    @Override
    public PageResponse<SourceFilterResponse> filter(SourceFilterRequest request) {
        log.info("=== Start filter");
        log.debug("(filter) request: {}", request);

        return sourceService.filter(request);
    }
}


