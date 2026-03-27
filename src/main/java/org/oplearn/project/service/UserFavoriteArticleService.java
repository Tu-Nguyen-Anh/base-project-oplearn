package org.oplearn.project.service;

import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.FavoriteArticleResponse;

public interface UserFavoriteArticleService {
    void addFavorite(Long userId, Long articleId);

    void removeFavorite(Long userId, Long articleId);

    PageResponse<FavoriteArticleResponse> getFavorites(Long userId, int page, int size);
}
