package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleViewHistoryResponse;
import org.oplearn.project.entity.article.ArticleViewHistory;
import org.oplearn.project.repository.ArticleViewHistoryRepository;
import org.oplearn.project.service.ArticleViewHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewHistoryServiceImpl implements ArticleViewHistoryService {
    private final ArticleViewHistoryRepository repository;

    @Transactional
    @Override
    public void saveView(Long userId, Long articleId) {
        log.info("=== Start saveView");
        log.debug("(saveView) userId: {}, articleId: {}", userId, articleId);

        ArticleViewHistory history = ArticleViewHistory.builder()
                .userId(userId)
                .articleId(articleId)
                .build();

        repository.save(history);
    }

    @Override
    public PageResponse<ArticleViewHistoryResponse> getViewHistory(Long userId, int page, int size) {
        log.info("=== Start getViewHistory");
        log.debug("(getViewHistory) userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleViewHistoryResponse> result = repository.findByUserId(userId, pageable);

        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }
}
