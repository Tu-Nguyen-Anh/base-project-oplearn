package org.oplearn.project.repository;

import org.oplearn.project.dto.response.article.ArticleViewHistoryResponse;
import org.oplearn.project.entity.article.ArticleViewHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleViewHistoryRepository extends BaseRepository<ArticleViewHistory> {

    @Query("""
            SELECT new org.oplearn.project.dto.response.article.ArticleViewHistoryResponse(
                h.id,
                h.userId,
                h.articleId,
                a.title,
                a.link,
                a.imageLink,
                a.pubDate,
                t.name,
                s.name,
                h.createdAt
            )
            FROM ArticleViewHistory h
            LEFT JOIN Article a ON h.articleId = a.id AND a.deleted = false
            LEFT JOIN Topic t ON a.topicId = t.id
            LEFT JOIN Source s ON t.sourceId = s.id
            WHERE h.userId = :userId
            ORDER BY h.createdAt DESC
            """)
    Page<ArticleViewHistoryResponse> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
