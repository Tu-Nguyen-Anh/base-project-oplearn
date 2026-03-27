package org.oplearn.project.service;

public interface BlogPostLikeService {
    void like(Long postId, Long userId);
    void unlike(Long postId, Long userId);
}
