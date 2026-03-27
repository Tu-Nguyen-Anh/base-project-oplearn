package org.oplearn.project.repository;

import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.entity.blog.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogPostRepository extends BaseRepository<BlogPost> {

    Optional<BlogPost> findByIdAndDeletedFalse(Long id);

    long countByUserIdAndDeletedFalse(Long userId);

    @Query("""
        SELECT new org.oplearn.project.dto.response.blog.BlogPostFilterResponse(
            p.id, p.userId, u.fullName, u.avatar, p.title, p.content, p.imageUrl, p.visibility, p.createdAt
        )
        FROM BlogPost p
        JOIN User u ON p.userId = u.id
        WHERE p.deleted = false
        AND p.visibility = 0
        AND (:keyword = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                             OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:authorId IS NULL OR p.userId = :authorId)
        ORDER BY p.createdAt DESC
    """)
    Page<BlogPostFilterResponse> filter(
        @Param("keyword") String keyword,
        @Param("authorId") Long authorId,
        Pageable pageable
    );

    @Query("""
        SELECT new org.oplearn.project.dto.response.blog.BlogPostFilterResponse(
            p.id, p.userId, u.fullName, u.avatar, p.title, p.content, p.imageUrl, p.visibility, p.createdAt
        )
        FROM BlogPost p
        JOIN User u ON p.userId = u.id
        WHERE p.deleted = false
        AND p.userId = :userId
        ORDER BY p.createdAt DESC
    """)
    Page<BlogPostFilterResponse> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
