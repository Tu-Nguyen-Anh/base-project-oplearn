package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.blog.BlogPostLike;
import org.oplearn.project.exception.base.blog.BlogPostAlreadyLikedException;
import org.oplearn.project.exception.base.blog.BlogPostLikeNotFoundException;
import org.oplearn.project.repository.BlogPostLikeRepository;
import org.oplearn.project.service.BlogPostLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostLikeServiceImpl implements BlogPostLikeService {

    private final BlogPostLikeRepository likeRepository;

    @Transactional
    @Override
    public void like(Long postId, Long userId) {
        log.debug("(like) postId: {}, userId: {}", postId, userId);

        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new BlogPostAlreadyLikedException();
        }

        likeRepository.save(BlogPostLike.builder()
                .postId(postId)
                .userId(userId)
                .build());
    }

    @Transactional
    @Override
    public void unlike(Long postId, Long userId) {
        log.debug("(unlike) postId: {}, userId: {}", postId, userId);

        BlogPostLike like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(BlogPostLikeNotFoundException::new);

        likeRepository.delete(like);
    }
}
