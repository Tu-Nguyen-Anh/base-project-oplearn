package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ArticleFilterRequest;
import org.oplearn.project.dto.request.ArticleRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.dto.response.article.ArticleResponse;
import org.oplearn.project.facade.ArticleFacadeService;
import org.oplearn.project.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleFacadeServiceImpl implements ArticleFacadeService {
    private final ArticleService articleService;

    @Transactional
    @Override
    public ArticleResponse create(ArticleRequest request) {
        log.info("=== Start create");
        log.debug("(create) request: {}", request);

        return articleService.create(request);
    }

    @Transactional
    @Override
    public ArticleResponse update(ArticleRequest request, Long id) {
        log.info("=== Start update");
        log.debug("(update) request: {}", request);

        return articleService.update(request, id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("=== Start delete");
        log.debug("(delete) id: {}", id);
        articleService.delete(id);
    }

    @Override
    public PageResponse<ArticleFilterResponse> filter(ArticleFilterRequest request) {
        log.info("=== Start filter");
        log.debug("(filter) request: {}", request);

        return articleService.filter(request);
    }
}



