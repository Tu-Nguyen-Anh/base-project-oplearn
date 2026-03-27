package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.BlogPostCommentRequest;
import org.oplearn.project.dto.request.BlogPostFilterRequest;
import org.oplearn.project.dto.request.BlogPostRequest;
import org.oplearn.project.dto.request.BlogPostShareRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostCommentResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.dto.response.user.UserProfileResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.facade.BlogPostFacadeService;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.BlogPostCommentService;
import org.oplearn.project.service.BlogPostLikeService;
import org.oplearn.project.service.BlogPostService;
import org.oplearn.project.service.BlogPostShareService;
import org.oplearn.project.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostFacadeServiceImpl implements BlogPostFacadeService {

    private final BlogPostService blogPostService;
    private final BlogPostLikeService likeService;
    private final BlogPostCommentService commentService;
    private final BlogPostShareService shareService;
    private final UserService userService;

    @Transactional
    @Override
    public BlogPostFilterResponse create(BlogPostRequest request) {
        log.info("=== Start create post");
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return blogPostService.create(currentUser.getId(), request);
    }

    @Transactional
    @Override
    public BlogPostFilterResponse update(Long postId, BlogPostRequest request) {
        log.info("=== Start update post");
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return blogPostService.update(postId, currentUser.getId(), request);
    }

    @Transactional
    @Override
    public void delete(Long postId) {
        log.info("=== Start delete post");
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        blogPostService.delete(postId, currentUser.getId());
    }

    @Override
    public BlogPostFilterResponse getDetail(Long postId) {
        log.info("=== Start getDetail post");
        Long currentUserId = getCurrentUserIdOrNull();
        return blogPostService.getDetail(postId, currentUserId);
    }

    @Override
    public PageResponse<BlogPostFilterResponse> filter(BlogPostFilterRequest request) {
        log.info("=== Start filter posts");
        return blogPostService.filter(request);
    }

    @Transactional
    @Override
    public void like(Long postId) {
        log.info("=== Start like post");
        blogPostService.checkExistById(postId);
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        likeService.like(postId, currentUser.getId());
    }

    @Transactional
    @Override
    public void unlike(Long postId) {
        log.info("=== Start unlike post");
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        likeService.unlike(postId, currentUser.getId());
    }

    @Transactional
    @Override
    public void share(Long postId, BlogPostShareRequest request) {
        log.info("=== Start share post");
        blogPostService.checkExistById(postId);
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        shareService.share(postId, currentUser.getId(), request);
    }

    @Transactional
    @Override
    public BlogPostCommentResponse addComment(Long postId, BlogPostCommentRequest request) {
        log.info("=== Start addComment");
        blogPostService.checkExistById(postId);
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return commentService.create(postId, currentUser.getId(), request);
    }

    @Transactional
    @Override
    public void deleteComment(Long postId, Long commentId) {
        log.info("=== Start deleteComment");
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        commentService.delete(commentId, currentUser.getId());
    }

    @Override
    public PageResponse<BlogPostCommentResponse> getComments(Long postId, int page, int size) {
        log.info("=== Start getComments");
        return commentService.getByPostId(postId, page, size);
    }

    @Override
    public PageResponse<BlogPostCommentResponse> getReplies(Long postId, Long commentId, int page, int size) {
        log.info("=== Start getReplies");
        return commentService.getReplies(commentId, page, size);
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId, int page, int size) {
        log.info("=== Start getUserProfile");
        User user = userService.getById(userId);
        long totalPosts = blogPostService.countByUserId(userId);
        PageResponse<BlogPostFilterResponse> posts = blogPostService.getByUserId(userId, page, size);

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .totalPosts(totalPosts)
                .posts(posts)
                .build();
    }

    private Long getCurrentUserIdOrNull() {
        try {
            return UserAuthenticated.getCurrentUserThrowUnAuthorized().getId();
        } catch (Exception e) {
            return null;
        }
    }
}
