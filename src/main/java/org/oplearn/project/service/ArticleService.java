package org.oplearn.project.service;

import org.oplearn.project.dto.request.ArticleFilterRequest;
import org.oplearn.project.dto.request.ArticleRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.dto.response.article.ArticleResponse;
import org.oplearn.project.entity.article.Article;

public interface ArticleService {
    Article getById(Long articleId);

    ArticleResponse create(ArticleRequest request);

    ArticleResponse update(ArticleRequest request, Long id);

    void delete(Long id);

    void checkLinkExists(String link);

    void save(Article article);

    PageResponse<ArticleFilterResponse> filter(ArticleFilterRequest request);

    ArticleResponse detail(Long id);

    Article checkExistById(Long id);
}



