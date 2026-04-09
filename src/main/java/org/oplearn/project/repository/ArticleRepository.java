package org.oplearn.project.repository;

import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.entity.article.Article;
import org.oplearn.project.repository.projection.DailyCountProjection;
import org.oplearn.project.repository.projection.MonthlyCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends BaseRepository<Article> {
    Article getByIdAndDeletedFalse(Long id);

    Optional<Article> findByTitleAndDeletedFalse(String title);

    boolean existsByLinkAndDeletedIsFalse(String link);

    @Query("""
        SELECT distinct new org.oplearn.project.dto.response.article.ArticleFilterResponse(
                a.id,
                a.title,
                a.link,
                a.guid,
                a.description,
                a.pubDate,
                a.imageLink,
                t.id,
                t.name,
                a.createdBy,
                a.createdAt,
                s.name
                )
              FROM Article a
              LEFT JOIN Topic t ON a.topicId = t.id
              LEFT JOIN Source s ON t.sourceId = s.id
              WHERE
                ( :keyword = '' OR
                LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(a.link) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                )
                AND a.deleted = false
                AND (:topicId IS NULL OR a.topicId = :topicId)
                AND (:sourceId IS NULL OR s.id= :sourceId)
              ORDER BY a.createdAt DESC
        """)
    Page<ArticleFilterResponse> filterWithoutDate(
            @Param("keyword") String keyword,
            @Param("topicId") Long topicId,
            @Param("sourceId") Long sourceId,
            Pageable pageable
    );

    @Query(value = """
            SELECT CAST(EXTRACT(MONTH FROM TO_TIMESTAMP(a.created_at / 1000.0)) AS INTEGER) AS month,
                   COUNT(*) AS count
            FROM articles a
            WHERE CAST(EXTRACT(YEAR FROM TO_TIMESTAMP(a.created_at / 1000.0)) AS INTEGER) = :year
              AND a.deleted = false
            GROUP BY 1
            ORDER BY 1
            """, nativeQuery = true)
    List<MonthlyCountProjection> countArticlesByMonth(@Param("year") int year);

    @Query(value = """
            SELECT CAST(EXTRACT(DAY FROM TO_TIMESTAMP(a.created_at / 1000.0)) AS INTEGER) AS day,
                   COUNT(*) AS count
            FROM articles a
            WHERE CAST(EXTRACT(YEAR FROM TO_TIMESTAMP(a.created_at / 1000.0)) AS INTEGER) = :year
              AND CAST(EXTRACT(MONTH FROM TO_TIMESTAMP(a.created_at / 1000.0)) AS INTEGER) = :month
              AND a.deleted = false
            GROUP BY 1
            ORDER BY 1
            """, nativeQuery = true)
    List<DailyCountProjection> countArticlesByDay(@Param("year") int year, @Param("month") int month);

    @Query(value = """
            SELECT s.id        AS source_id,
                   s.name      AS source_name,
                   CAST(EXTRACT(MONTH FROM TO_TIMESTAMP(a.created_at / 1000.0)) AS INTEGER) AS month,
                   COUNT(*)    AS count
            FROM articles a
                     JOIN topics t ON a.topic_id = t.id
                     JOIN sources s ON t.source_id = s.id
            WHERE CAST(EXTRACT(YEAR FROM TO_TIMESTAMP(a.created_at / 1000.0)) AS INTEGER) = :year
              AND a.deleted = false
            GROUP BY s.id, s.name, 3
            ORDER BY s.id, 3
            """, nativeQuery = true)
    List<Object[]> countArticlesBySourceAndMonth(@Param("year") int year);

    @Query("""
        SELECT new org.oplearn.project.dto.response.article.ArticleFilterResponse(
                a.id,
                a.title,
                a.link,
                a.guid,
                a.description,
                a.pubDate,
                a.imageLink,
                t.id,
                t.name,
                a.createdBy,
                a.createdAt,
                s.name
                )
              FROM Article a
              LEFT JOIN Topic t ON a.topicId = t.id
              LEFT JOIN Source s ON t.sourceId = s.id
              WHERE
                ( :keyword = '' OR
                LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(a.link) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND a.deleted = false
                AND (:topicId IS NULL OR a.topicId = :topicId)
                AND (:sourceId IS NULL OR s.id= :sourceId)
                AND (:fromPubDateTimestamp IS NULL OR a.createdAt >= :fromPubDateTimestamp)
                AND (:toPubDateTimestamp IS NULL OR a.createdAt <= :toPubDateTimestamp)
              ORDER BY a.createdAt DESC
        """)
    Page<ArticleFilterResponse> filter(
            @Param("keyword") String keyword,
            @Param("topicId") Long topicId,
            @Param("sourceId") Long sourceId,
            @Param("fromPubDateTimestamp") Long fromPubDateTimestamp,
            @Param("toPubDateTimestamp") Long toPubDateTimestamp,
            Pageable pageable
    );

    @Query("""
        SELECT new org.oplearn.project.dto.response.article.ArticleFilterResponse(
                a.id,
                a.title,
                a.link,
                a.guid,
                a.description,
                a.pubDate,
                a.imageLink,
                t.id,
                t.name,
                a.createdBy,
                a.createdAt,
                s.name
                )
              FROM Article a
              LEFT JOIN Topic t ON a.topicId = t.id
              LEFT JOIN Source s ON t.sourceId = s.id
              WHERE
                ( :keyword = '' OR
                LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(a.link) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND a.deleted = false
                AND a.topicId IN :followedTopicIds
                AND (:fromPubDateTimestamp IS NULL OR a.createdAt >= :fromPubDateTimestamp)
                AND (:toPubDateTimestamp IS NULL OR a.createdAt <= :toPubDateTimestamp)
              ORDER BY a.createdAt DESC
        """)
    Page<ArticleFilterResponse> filterByFollowedTopics(
            @Param("keyword") String keyword,
            @Param("followedTopicIds") Collection<Long> followedTopicIds,
            @Param("fromPubDateTimestamp") Long fromPubDateTimestamp,
            @Param("toPubDateTimestamp") Long toPubDateTimestamp,
            Pageable pageable
    );
}

