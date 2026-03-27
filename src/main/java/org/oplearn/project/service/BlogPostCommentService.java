package org.oplearn.project.service;

import org.oplearn.project.dto.request.BlogPostCommentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostCommentResponse;
import org.oplearn.project.entity.blog.BlogPostComment;

public interface BlogPostCommentService {

    BlogPostCommentResponse create(Long postId, Long userId, BlogPostCommentRequest request);

    void delete(Long commentId, Long userId);

    PageResponse<BlogPostCommentResponse> getByPostId(Long postId, int page, int size);

    PageResponse<BlogPostCommentResponse> getReplies(Long commentId, int page, int size);

    BlogPostComment checkExistById(Long commentId);
}
