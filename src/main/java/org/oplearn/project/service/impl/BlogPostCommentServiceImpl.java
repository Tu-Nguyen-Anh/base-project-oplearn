package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.BlogPostCommentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostCommentResponse;
import org.oplearn.project.entity.blog.BlogPostComment;
import org.oplearn.project.exception.base.ForbiddenException;
import org.oplearn.project.exception.base.article.CommentNotFoundException;
import org.oplearn.project.repository.BlogPostCommentRepository;
import org.oplearn.project.service.BlogPostCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostCommentServiceImpl implements BlogPostCommentService {

    private final BlogPostCommentRepository commentRepository;

    @Transactional
    @Override
    public BlogPostCommentResponse create(Long postId, Long userId, BlogPostCommentRequest request) {
        log.debug("(create) postId: {}, userId: {}", postId, userId);

        BlogPostComment comment = BlogPostComment.builder()
                .postId(postId)
                .userId(userId)
                .parentCommentId(request.getParentCommentId())
                .content(request.getContent())
                .deleted(false)
                .build();

        comment = commentRepository.save(comment);

        return BlogPostCommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .parentCommentId(comment.getParentCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Transactional
    @Override
    public void delete(Long commentId, Long userId) {
        log.debug("(delete) commentId: {}, userId: {}", commentId, userId);

        BlogPostComment comment = checkExistById(commentId);
        if (!comment.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    @Override
    public PageResponse<BlogPostCommentResponse> getByPostId(Long postId, int page, int size) {
        log.debug("(getByPostId) postId: {}", postId);

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostCommentResponse> result = commentRepository.findTopLevelByPostId(postId, pageable);
        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }

    @Override
    public PageResponse<BlogPostCommentResponse> getReplies(Long commentId, int page, int size) {
        log.debug("(getReplies) commentId: {}", commentId);

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostCommentResponse> result = commentRepository.findRepliesByParentId(commentId, pageable);
        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }

    @Override
    public BlogPostComment checkExistById(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }
}
