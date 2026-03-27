package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ArticleFilterRequest;
import org.oplearn.project.dto.request.ArticleRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.dto.response.article.ArticleResponse;
import org.oplearn.project.dto.response.article.ArticleViewHistoryResponse;
import org.oplearn.project.dto.response.article.FavoriteArticleResponse;
import org.oplearn.project.facade.ArticleFacadeService;
import org.oplearn.project.service.ArticleService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleFacadeService articleFacadeService;
    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseGeneral<ArticleResponse> create(
            @RequestBody @Validated ArticleRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(create) request: {}", request);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                articleFacadeService.create(request)
        );
    }

    @PutMapping("{id}")
    public ResponseGeneral<ArticleResponse> update(
            @RequestBody @Validated ArticleRequest request,
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(update) request: {}", request);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                articleFacadeService.update(request, id)
        );
    }

    @DeleteMapping("{id}")
    public ResponseGeneral<Void> delete(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(delete) id: {}", id);

        articleFacadeService.delete(id);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/exist-link")
    public ResponseGeneral<Void> checkLinkExist(
            @RequestParam String link,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(checkLinkExist) link: {}", link);

        articleService.checkLinkExists(link);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("{id}")
    public ResponseGeneral<ArticleResponse> detail(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(detail) id: {}", id);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                articleService.detail(id)
        );
    }

    @PostMapping("/filter")
    public ResponseGeneral<PageResponse<ArticleFilterResponse>> filter(
            @RequestBody ArticleFilterRequest articleFilterRequest,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("Start filter article: {}", articleFilterRequest);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                articleFacadeService.filter(articleFilterRequest));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{articleId}/favorites")
    public ResponseGeneral<Void> addFavorite(
            @PathVariable Long articleId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(addFavorite) articleId: {}", articleId);

        articleFacadeService.addFavorite(articleId);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @DeleteMapping("/{articleId}/favorites")
    public ResponseGeneral<Void> removeFavorite(
            @PathVariable Long articleId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(removeFavorite) articleId: {}", articleId);

        articleFacadeService.removeFavorite(articleId);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/favorites")
    public ResponseGeneral<PageResponse<FavoriteArticleResponse>> getFavorites(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getFavorites) page: {}, size: {}", page, size);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                articleFacadeService.getFavorites(page, size));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{articleId}/view")
    public ResponseGeneral<Void> viewArticle(
            @PathVariable Long articleId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(viewArticle) articleId: {}", articleId);

        articleFacadeService.viewArticle(articleId);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/view-history")
    public ResponseGeneral<PageResponse<ArticleViewHistoryResponse>> getViewHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getViewHistory) page: {}, size: {}", page, size);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                articleFacadeService.getViewHistory(page, size));
    }
}



