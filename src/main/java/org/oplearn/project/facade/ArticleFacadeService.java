package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ArticleFilterRequest;
import org.oplearn.project.dto.request.ArticleRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.dto.response.article.ArticleResponse;

public interface ArticleFacadeService {
    ArticleResponse create(ArticleRequest request);

    ArticleResponse update(ArticleRequest request, Long id);

    void delete(Long id);

    PageResponse<ArticleFilterResponse> filter(ArticleFilterRequest request);
}



