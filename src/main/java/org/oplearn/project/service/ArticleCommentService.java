package org.oplearn.project.service;

import org.oplearn.project.dto.request.CommentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.CommentResponse;
import org.oplearn.project.entity.article.ArticleComment;

public interface ArticleCommentService {
    CommentResponse create(Long articleId, Long userId, CommentRequest request);

    void delete(Long commentId, Long currentUserId);

    PageResponse<CommentResponse> getByArticleId(Long articleId, int page, int size);

    ArticleComment checkExistById(Long commentId);
}
