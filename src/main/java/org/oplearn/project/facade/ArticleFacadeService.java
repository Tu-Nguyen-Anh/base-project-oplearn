package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ArticleFilterRequest;
import org.oplearn.project.dto.request.ArticleRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.dto.response.article.ArticleResponse;
import org.oplearn.project.dto.response.article.ArticleViewHistoryResponse;
import org.oplearn.project.dto.response.article.FavoriteArticleResponse;

public interface ArticleFacadeService {
    ArticleResponse create(ArticleRequest request);

    ArticleResponse update(ArticleRequest request, Long id);

    void delete(Long id);

    PageResponse<ArticleFilterResponse> filter(ArticleFilterRequest request);

    void addFavorite(Long articleId);

    void removeFavorite(Long articleId);

    PageResponse<FavoriteArticleResponse> getFavorites(int page, int size);

    void viewArticle(Long articleId);

    PageResponse<ArticleViewHistoryResponse> getViewHistory(int page, int size);
}



