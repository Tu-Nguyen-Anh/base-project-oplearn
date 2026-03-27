package org.oplearn.project.repository;

import org.oplearn.project.dto.response.article.CommentResponse;
import org.oplearn.project.entity.article.ArticleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleCommentRepository extends BaseRepository<ArticleComment> {

    Optional<ArticleComment> findByIdAndDeletedFalse(Long id);

    @Query("""
            SELECT new org.oplearn.project.dto.response.article.CommentResponse(
                c.id,
                c.articleId,
                c.userId,
                u.username,
                u.fullName,
                u.avatar,
                c.content,
                c.createdAt
            )
            FROM ArticleComment c
            LEFT JOIN User u ON c.userId = u.id
            WHERE c.articleId = :articleId
              AND c.deleted = false
            ORDER BY c.createdAt ASC
            """)
    Page<CommentResponse> findByArticleId(@Param("articleId") Long articleId, Pageable pageable);
}
