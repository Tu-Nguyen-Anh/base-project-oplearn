package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ArticleFilterRequest;
import org.oplearn.project.dto.request.ArticleRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.dto.response.article.ArticleResponse;
import org.oplearn.project.dto.response.article.ArticleViewHistoryResponse;
import org.oplearn.project.dto.response.article.FavoriteArticleResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.facade.ArticleFacadeService;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.ArticleService;
import org.oplearn.project.service.ArticleViewHistoryService;
import org.oplearn.project.service.UserFavoriteArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleFacadeServiceImpl implements ArticleFacadeService {
    private final ArticleService articleService;
    private final UserFavoriteArticleService userFavoriteArticleService;
    private final ArticleViewHistoryService articleViewHistoryService;

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

    @Transactional
    @Override
    public void addFavorite(Long articleId) {
        log.info("=== Start addFavorite");
        log.debug("(addFavorite) articleId: {}", articleId);

        articleService.checkExistById(articleId);
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        userFavoriteArticleService.addFavorite(currentUser.getId(), articleId);
    }

    @Transactional
    @Override
    public void removeFavorite(Long articleId) {
        log.info("=== Start removeFavorite");
        log.debug("(removeFavorite) articleId: {}", articleId);

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        userFavoriteArticleService.removeFavorite(currentUser.getId(), articleId);
    }

    @Override
    public PageResponse<FavoriteArticleResponse> getFavorites(int page, int size) {
        log.info("=== Start getFavorites");

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return userFavoriteArticleService.getFavorites(currentUser.getId(), page, size);
    }

    @Transactional
    @Override
    public void viewArticle(Long articleId) {
        log.info("=== Start viewArticle");
        log.debug("(viewArticle) articleId: {}", articleId);

        articleService.checkExistById(articleId);
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        articleViewHistoryService.saveView(currentUser.getId(), articleId);
    }

    @Override
    public PageResponse<ArticleViewHistoryResponse> getViewHistory(int page, int size) {
        log.info("=== Start getViewHistory");

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return articleViewHistoryService.getViewHistory(currentUser.getId(), page, size);
    }
}



