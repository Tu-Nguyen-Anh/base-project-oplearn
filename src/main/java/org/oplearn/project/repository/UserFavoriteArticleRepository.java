package org.oplearn.project.repository;

import org.oplearn.project.dto.response.article.FavoriteArticleResponse;
import org.oplearn.project.entity.article.UserFavoriteArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserFavoriteArticleRepository extends BaseRepository<UserFavoriteArticle> {

    @Query("""
            SELECT new org.oplearn.project.dto.response.article.FavoriteArticleResponse(
                f.id,
                f.userId,
                f.articleId,
                a.title,
                a.link,
                a.imageLink,
                a.pubDate,
                t.name,
                s.name,
                f.createdAt
            )
            FROM UserFavoriteArticle f
            LEFT JOIN Article a ON f.articleId = a.id AND a.deleted = false
            LEFT JOIN Topic t ON a.topicId = t.id
            LEFT JOIN Source s ON t.sourceId = s.id
            WHERE f.userId = :userId
              AND f.deleted = false
            ORDER BY f.createdAt DESC
            """)
    Page<FavoriteArticleResponse> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
            SELECT f FROM UserFavoriteArticle f
            WHERE f.userId = :userId
              AND f.articleId = :articleId
              AND f.deleted = false
            """)
    Optional<UserFavoriteArticle> findByUserIdAndArticleId(@Param("userId") Long userId,
                                                            @Param("articleId") Long articleId);

    boolean existsByUserIdAndArticleIdAndDeletedFalse(Long userId, Long articleId);
}
