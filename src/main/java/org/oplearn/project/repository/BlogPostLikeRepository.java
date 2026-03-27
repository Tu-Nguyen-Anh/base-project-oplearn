package org.oplearn.project.repository;

import org.oplearn.project.entity.blog.BlogPostLike;

import java.util.Optional;

public interface BlogPostLikeRepository extends BaseRepository<BlogPostLike> {

    long countByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Optional<BlogPostLike> findByPostIdAndUserId(Long postId, Long userId);
}
