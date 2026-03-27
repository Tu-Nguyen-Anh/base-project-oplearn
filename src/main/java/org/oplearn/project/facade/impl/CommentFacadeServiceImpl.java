package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CommentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.CommentResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.facade.CommentFacadeService;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.ArticleCommentService;
import org.oplearn.project.service.ArticleService;
import org.oplearn.project.service.NotificationService;
import org.oplearn.project.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentFacadeServiceImpl implements CommentFacadeService {
    private final ArticleCommentService commentService;
    private final NotificationService notificationService;
    private final ArticleService articleService;
    private final UserService userService;

    @Transactional
    @Override
    public CommentResponse create(Long articleId, CommentRequest request) {
        log.info("=== Start create comment");
        log.debug("(create) articleId: {}", articleId);

        articleService.checkExistById(articleId);

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        CommentResponse response = commentService.create(articleId, currentUser.getId(), request);

        List<Long> mentionedUserIds = request.getMentionedUserIds();
        if (mentionedUserIds != null && !mentionedUserIds.isEmpty()) {
            mentionedUserIds.stream()
                    .distinct()
                    .filter(mentionedUserId -> !mentionedUserId.equals(currentUser.getId()))
                    .forEach(mentionedUserId -> {
                        try {
                            userService.checkExistById(mentionedUserId);
                            notificationService.createMentionNotification(
                                    mentionedUserId,
                                    currentUser.getId(),
                                    currentUser.getFullName(),
                                    articleId,
                                    response.getId()
                            );
                        } catch (Exception e) {
                            log.warn("(create) skip notification for userId: {}, reason: {}", mentionedUserId, e.getMessage());
                        }
                    });
        }

        return response;
    }

    @Transactional
    @Override
    public void delete(Long commentId) {
        log.info("=== Start delete comment");
        log.debug("(delete) commentId: {}", commentId);

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        commentService.delete(commentId, currentUser.getId());
    }

    @Override
    public PageResponse<CommentResponse> getByArticleId(Long articleId, int page, int size) {
        log.info("=== Start getByArticleId");
        log.debug("(getByArticleId) articleId: {}, page: {}, size: {}", articleId, page, size);

        return commentService.getByArticleId(articleId, page, size);
    }
}
