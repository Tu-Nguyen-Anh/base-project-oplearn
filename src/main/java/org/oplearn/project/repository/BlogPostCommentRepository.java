package org.oplearn.project.repository;

import org.oplearn.project.dto.response.blog.BlogPostCommentResponse;
import org.oplearn.project.entity.blog.BlogPostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogPostCommentRepository extends BaseRepository<BlogPostComment> {

    Optional<BlogPostComment> findByIdAndDeletedFalse(Long id);

    long countByPostIdAndDeletedFalse(Long postId);

    @Query("""
        SELECT new org.oplearn.project.dto.response.blog.BlogPostCommentResponse(
            c.id, c.postId, c.userId, u.fullName, u.avatar, c.parentCommentId, c.content, c.createdAt
        )
        FROM BlogPostComment c
        JOIN User u ON c.userId = u.id
        WHERE c.postId = :postId AND c.deleted = false AND c.parentCommentId IS NULL
        ORDER BY c.createdAt ASC
    """)
    Page<BlogPostCommentResponse> findTopLevelByPostId(@Param("postId") Long postId, Pageable pageable);

    @Query("""
        SELECT new org.oplearn.project.dto.response.blog.BlogPostCommentResponse(
            c.id, c.postId, c.userId, u.fullName, u.avatar, c.parentCommentId, c.content, c.createdAt
        )
        FROM BlogPostComment c
        JOIN User u ON c.userId = u.id
        WHERE c.parentCommentId = :parentCommentId AND c.deleted = false
        ORDER BY c.createdAt ASC
    """)
    Page<BlogPostCommentResponse> findRepliesByParentId(@Param("parentCommentId") Long parentCommentId, Pageable pageable);
}
