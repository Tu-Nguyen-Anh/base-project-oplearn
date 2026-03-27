package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.FavoriteArticleResponse;
import org.oplearn.project.entity.article.UserFavoriteArticle;
import org.oplearn.project.exception.base.article.ArticleAlreadyFavoritedException;
import org.oplearn.project.exception.base.article.FavoriteNotFoundException;
import org.oplearn.project.repository.UserFavoriteArticleRepository;
import org.oplearn.project.service.UserFavoriteArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavoriteArticleServiceImpl implements UserFavoriteArticleService {
    private final UserFavoriteArticleRepository repository;

    @Transactional
    @Override
    public void addFavorite(Long userId, Long articleId) {
        log.info("=== Start addFavorite");
        log.debug("(addFavorite) userId: {}, articleId: {}", userId, articleId);

        if (repository.existsByUserIdAndArticleIdAndDeletedFalse(userId, articleId)) {
            throw new ArticleAlreadyFavoritedException();
        }

        UserFavoriteArticle favorite = UserFavoriteArticle.builder()
                .userId(userId)
                .articleId(articleId)
                .deleted(false)
                .build();

        repository.save(favorite);
    }

    @Transactional
    @Override
    public void removeFavorite(Long userId, Long articleId) {
        log.info("=== Start removeFavorite");
        log.debug("(removeFavorite) userId: {}, articleId: {}", userId, articleId);

        UserFavoriteArticle favorite = repository.findByUserIdAndArticleId(userId, articleId)
                .orElseThrow(FavoriteNotFoundException::new);

        favorite.setDeleted(true);
        repository.save(favorite);
    }

    @Override
    public PageResponse<FavoriteArticleResponse> getFavorites(Long userId, int page, int size) {
        log.info("=== Start getFavorites");
        log.debug("(getFavorites) userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<FavoriteArticleResponse> result = repository.findByUserId(userId, pageable);

        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }
}
