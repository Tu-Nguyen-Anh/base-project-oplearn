package org.oplearn.project.service;

import org.oplearn.project.dto.request.SourceFilterRequest;
import org.oplearn.project.dto.request.SourceRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.source.SourceFilterResponse;
import org.oplearn.project.dto.response.source.SourceResponse;
import org.oplearn.project.dto.response.source.SourceWithTopicsResponse;
import org.oplearn.project.entity.source.Source;

import java.util.List;

public interface SourceService {
    Source getById(Long sourceId);

    SourceResponse create(SourceRequest request);

    SourceResponse update(SourceRequest request, Long id);

    void delete(Long id);

    void checkNameExists(String name);

    void checkUrlExists(String url);

    void save(Source source);

    PageResponse<SourceFilterResponse> filter(SourceFilterRequest request);

    SourceResponse detail(Long id);

    Source checkExistById(Long id);

    List<SourceWithTopicsResponse> getAllWithTopics();
}

