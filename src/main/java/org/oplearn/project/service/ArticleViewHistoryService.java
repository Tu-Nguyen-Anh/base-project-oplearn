package org.oplearn.project.service;

import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleViewHistoryResponse;

public interface ArticleViewHistoryService {
    void saveView(Long userId, Long articleId);

    PageResponse<ArticleViewHistoryResponse> getViewHistory(Long userId, int page, int size);
}
