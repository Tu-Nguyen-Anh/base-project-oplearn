package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.BlogPostFilterRequest;
import org.oplearn.project.dto.request.BlogPostRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.entity.blog.BlogPost;
import org.oplearn.project.exception.base.ForbiddenException;
import org.oplearn.project.exception.base.blog.BlogPostNotFoundException;
import org.oplearn.project.repository.BlogPostCommentRepository;
import org.oplearn.project.repository.BlogPostLikeRepository;
import org.oplearn.project.repository.BlogPostRepository;
import org.oplearn.project.repository.BlogPostShareRepository;
import org.oplearn.project.service.BlogPostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.BLANK;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostLikeRepository likeRepository;
    private final BlogPostCommentRepository commentRepository;
    private final BlogPostShareRepository shareRepository;

    @Transactional
    @Override
    public BlogPostFilterResponse create(Long userId, BlogPostRequest request) {
        log.debug("(create) userId: {}", userId);

        BlogPost post = BlogPost.builder()
                .userId(userId)
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .visibility(request.getVisibility())
                .deleted(false)
                .build();

        post = blogPostRepository.save(post);
        return buildFilterResponse(post, userId);
    }

    @Transactional
    @Override
    public BlogPostFilterResponse update(Long postId, Long userId, BlogPostRequest request) {
        log.debug("(update) postId: {}, userId: {}", postId, userId);

        BlogPost post = checkExistById(postId);
        if (!post.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setVisibility(request.getVisibility());

        post = blogPostRepository.save(post);
        return buildFilterResponse(post, userId);
    }

    @Transactional
    @Override
    public void delete(Long postId, Long userId) {
        log.debug("(delete) postId: {}, userId: {}", postId, userId);

        BlogPost post = checkExistById(postId);
        if (!post.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        post.setDeleted(true);
        blogPostRepository.save(post);
    }

    @Override
    public BlogPostFilterResponse getDetail(Long postId, Long currentUserId) {
        log.debug("(getDetail) postId: {}", postId);

        BlogPost post = checkExistById(postId);
        BlogPostFilterResponse response = buildFilterResponse(post, currentUserId);
        if (currentUserId != null) {
            response.setLiked(likeRepository.existsByPostIdAndUserId(postId, currentUserId));
        }
        return response;
    }

    @Override
    public PageResponse<BlogPostFilterResponse> filter(BlogPostFilterRequest request) {
        log.debug("(filter) request: {}", request);

        int page = Objects.requireNonNullElse(request.getPage(), 0);
        int size = Objects.requireNonNullElse(request.getSize(), 10);
        String keyword = Objects.requireNonNullElse(request.getKeyword(), BLANK);

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostFilterResponse> rawPage = blogPostRepository.filter(keyword, request.getAuthorId(), pageable);

        List<BlogPostFilterResponse> enriched = enrichWithCounts(rawPage.getContent(), null);
        return PageResponse.of(enriched, (int) rawPage.getTotalElements());
    }

    @Override
    public PageResponse<BlogPostFilterResponse> getByUserId(Long userId, int page, int size) {
        log.debug("(getByUserId) userId: {}", userId);

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostFilterResponse> rawPage = blogPostRepository.findAllByUserId(userId, pageable);

        List<BlogPostFilterResponse> enriched = enrichWithCounts(rawPage.getContent(), null);
        return PageResponse.of(enriched, (int) rawPage.getTotalElements());
    }

    @Override
    public BlogPost checkExistById(Long postId) {
        return blogPostRepository.findByIdAndDeletedFalse(postId)
                .orElseThrow(BlogPostNotFoundException::new);
    }

    @Override
    public long countByUserId(Long userId) {
        return blogPostRepository.countByUserIdAndDeletedFalse(userId);
    }

    private BlogPostFilterResponse buildFilterResponse(BlogPost post, Long currentUserId) {
        BlogPostFilterResponse response = new BlogPostFilterResponse(
                post.getId(), post.getUserId(), null, null,
                post.getTitle(), post.getContent(), post.getImageUrl(),
                post.getVisibility(), post.getCreatedAt()
        );
        response.setLikeCount(likeRepository.countByPostId(post.getId()));
        response.setCommentCount(commentRepository.countByPostIdAndDeletedFalse(post.getId()));
        response.setShareCount(shareRepository.countByPostId(post.getId()));
        if (currentUserId != null) {
            response.setLiked(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
        }
        return response;
    }

    private List<BlogPostFilterResponse> enrichWithCounts(List<BlogPostFilterResponse> posts, Long currentUserId) {
        posts.forEach(post -> {
            post.setLikeCount(likeRepository.countByPostId(post.getId()));
            post.setCommentCount(commentRepository.countByPostIdAndDeletedFalse(post.getId()));
            post.setShareCount(shareRepository.countByPostId(post.getId()));
            if (currentUserId != null) {
                post.setLiked(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
            }
        });
        return posts;
    }
}
