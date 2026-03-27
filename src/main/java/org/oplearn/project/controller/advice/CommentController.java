package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CommentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.article.CommentResponse;
import org.oplearn.project.facade.CommentFacadeService;
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
public class CommentController {
    private final CommentFacadeService commentFacadeService;
    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{articleId}/comments")
    public ResponseGeneral<CommentResponse> create(
            @PathVariable Long articleId,
            @RequestBody @Validated CommentRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(create comment) articleId: {}", articleId);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                commentFacadeService.create(articleId, request)
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseGeneral<Void> delete(
            @PathVariable Long commentId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(delete comment) commentId: {}", commentId);

        commentFacadeService.delete(commentId);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/{articleId}/comments")
    public ResponseGeneral<PageResponse<CommentResponse>> getByArticleId(
            @PathVariable Long articleId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getComments) articleId: {}, page: {}, size: {}", articleId, page, size);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                commentFacadeService.getByArticleId(articleId, page, size)
        );
    }
}
