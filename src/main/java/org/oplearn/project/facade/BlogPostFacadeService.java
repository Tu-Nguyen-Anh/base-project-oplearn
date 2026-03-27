package org.oplearn.project.facade;

import org.oplearn.project.dto.request.BlogPostCommentRequest;
import org.oplearn.project.dto.request.BlogPostFilterRequest;
import org.oplearn.project.dto.request.BlogPostRequest;
import org.oplearn.project.dto.request.BlogPostShareRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostCommentResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.dto.response.user.UserProfileResponse;

public interface BlogPostFacadeService {

    BlogPostFilterResponse create(BlogPostRequest request);

    BlogPostFilterResponse update(Long postId, BlogPostRequest request);

    void delete(Long postId);

    BlogPostFilterResponse getDetail(Long postId);

    PageResponse<BlogPostFilterResponse> filter(BlogPostFilterRequest request);

    void like(Long postId);

    void unlike(Long postId);

    void share(Long postId, BlogPostShareRequest request);

    BlogPostCommentResponse addComment(Long postId, BlogPostCommentRequest request);

    void deleteComment(Long postId, Long commentId);

    PageResponse<BlogPostCommentResponse> getComments(Long postId, int page, int size);

    PageResponse<BlogPostCommentResponse> getReplies(Long postId, Long commentId, int page, int size);

    UserProfileResponse getUserProfile(Long userId, int page, int size);
}
