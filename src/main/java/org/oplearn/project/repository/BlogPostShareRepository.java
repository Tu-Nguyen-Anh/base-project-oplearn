package org.oplearn.project.repository;

import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.entity.blog.BlogPostShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogPostShareRepository extends BaseRepository<BlogPostShare> {

    long countByPostId(Long postId);

    @Query("""
        SELECT new org.oplearn.project.dto.response.blog.BlogPostFilterResponse(
            p.id, p.userId, u.fullName, u.avatar, p.title, p.content, p.imageUrl, p.visibility, p.createdAt
        )
        FROM BlogPostShare s
        JOIN BlogPost p ON s.postId = p.id
        JOIN User u ON p.userId = u.id
        WHERE s.userId = :userId AND p.deleted = false
        ORDER BY s.createdAt DESC
    """)
    Page<BlogPostFilterResponse> findSharedByUserId(@Param("userId") Long userId, Pageable pageable);
}
