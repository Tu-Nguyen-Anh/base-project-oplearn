package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.BlogPostCommentRequest;
import org.oplearn.project.dto.request.BlogPostFilterRequest;
import org.oplearn.project.dto.request.BlogPostRequest;
import org.oplearn.project.dto.request.BlogPostShareRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.blog.BlogPostCommentResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.dto.response.user.UserProfileResponse;
import org.oplearn.project.facade.BlogPostFacadeService;
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
@RequestMapping("/api/v1/posts")
public class BlogPostController {

    private final BlogPostFacadeService blogPostFacadeService;
    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseGeneral<BlogPostFilterResponse> create(
            @RequestBody @Validated BlogPostRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(create) request: {}", request);
        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.create(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseGeneral<BlogPostFilterResponse> update(
            @PathVariable Long id,
            @RequestBody @Validated BlogPostRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(update) id: {}", id);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseGeneral<Void> delete(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(delete) id: {}", id);
        blogPostFacadeService.delete(id);
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @GetMapping("/{id}")
    public ResponseGeneral<BlogPostFilterResponse> getDetail(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getDetail) id: {}", id);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.getDetail(id)
        );
    }

    @PostMapping("/filter")
    public ResponseGeneral<PageResponse<BlogPostFilterResponse>> filter(
            @RequestBody BlogPostFilterRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(filter) request: {}", request);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.filter(request)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/like")
    public ResponseGeneral<Void> like(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(like) id: {}", id);
        blogPostFacadeService.like(id);
        return ResponseGeneral.ofCreated(messageService.getMessage(SUCCESS, language));
    }

    @DeleteMapping("/{id}/like")
    public ResponseGeneral<Void> unlike(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(unlike) id: {}", id);
        blogPostFacadeService.unlike(id);
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/share")
    public ResponseGeneral<Void> share(
            @PathVariable Long id,
            @RequestBody(required = false) BlogPostShareRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(share) id: {}", id);
        blogPostFacadeService.share(id, request);
        return ResponseGeneral.ofCreated(messageService.getMessage(SUCCESS, language));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{postId}/comments")
    public ResponseGeneral<BlogPostCommentResponse> addComment(
            @PathVariable Long postId,
            @RequestBody @Validated BlogPostCommentRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(addComment) postId: {}", postId);
        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.addComment(postId, request)
        );
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseGeneral<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(deleteComment) postId: {}, commentId: {}", postId, commentId);
        blogPostFacadeService.deleteComment(postId, commentId);
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @GetMapping("/{postId}/comments")
    public ResponseGeneral<PageResponse<BlogPostCommentResponse>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getComments) postId: {}", postId);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.getComments(postId, page, size)
        );
    }

    @GetMapping("/{postId}/comments/{commentId}/replies")
    public ResponseGeneral<PageResponse<BlogPostCommentResponse>> getReplies(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getReplies) postId: {}, commentId: {}", postId, commentId);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.getReplies(postId, commentId, page, size)
        );
    }

    @GetMapping("/profile/{userId}")
    public ResponseGeneral<UserProfileResponse> getUserProfile(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getUserProfile) userId: {}", userId);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                blogPostFacadeService.getUserProfile(userId, page, size)
        );
    }
}
