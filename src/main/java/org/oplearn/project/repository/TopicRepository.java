package org.oplearn.project.repository;

import jakarta.transaction.Transactional;
import org.hibernate.sql.Update;
import org.oplearn.project.dto.response.topic.TopicFilterResponse;
import org.oplearn.project.entity.topic.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends BaseRepository<Topic> {
    Topic getByIdAndDeletedFalse(Long id);

    Optional<Topic> findByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedIsFalse(String name);

    boolean existsByUrlAndDeletedIsFalse(String url);

    @Query("""
        SELECT distinct new org.oplearn.project.dto.response.topic.TopicFilterResponse(
                t.id,
                t.name,
                t.url,
                t.rssUrl,
                t.description,
                t.sourceId,
                s.name,
                t.createdBy,
                t.createdAt
                )
              FROM Topic t
              LEFT JOIN Source s ON t.sourceId = s.id
              WHERE
                ( :keyword = '' OR
                LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(t.url) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(t.rssUrl) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                )
                AND t.deleted = false
                AND (:sourceId IS NULL OR t.sourceId = :sourceId)
              ORDER BY t.createdAt DESC
        """)
    Page<TopicFilterResponse> filter(
            @Param("keyword") String keyword,
            @Param("sourceId") Long sourceId,
            Pageable pageable
    );

    @Query("""
        SELECT t
        FROM Topic t
        WHERE t.deleted = false
        AND t.sourceId = :sourceId
        ORDER BY t.createdAt DESC
        """)
    List<Topic> findBySourceIdAndDeletedFalse(@Param("sourceId") Long sourceId);


    @Transactional
    @Modifying
    @Query("UPDATE Topic t SET t.deleted = true WHERE t.sourceId = :sourceId")
    void deleteBySourceId(@Param("sourceId") Long sourceId);
}

