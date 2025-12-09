package org.oplearn.project.facade;

import org.oplearn.project.dto.request.SourceFilterRequest;
import org.oplearn.project.dto.request.SourceRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.source.SourceFilterResponse;
import org.oplearn.project.dto.response.source.SourceResponse;

public interface SourceFacadeService {
    SourceResponse create(SourceRequest request);

    SourceResponse update(SourceRequest request, Long id);

    void delete(Long id);

    PageResponse<SourceFilterResponse> filter(SourceFilterRequest request);
}


