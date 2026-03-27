package org.oplearn.project.facade;

import org.oplearn.project.dto.request.CommentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.CommentResponse;

public interface CommentFacadeService {
    CommentResponse create(Long articleId, CommentRequest request);

    void delete(Long commentId);

    PageResponse<CommentResponse> getByArticleId(Long articleId, int page, int size);
}
