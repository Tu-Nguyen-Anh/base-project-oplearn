package org.oplearn.project.repository;

import org.oplearn.project.dto.response.source.SourceFilterResponse;
import org.oplearn.project.entity.source.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SourceRepository extends BaseRepository<Source> {
    Source getByIdAndDeletedFalse(Long id);

    Optional<Source> findByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedIsFalse(String name);

    boolean existsByUrlAndDeletedIsFalse(String url);

    @Query("""
        SELECT distinct new org.oplearn.project.dto.response.source.SourceFilterResponse(
                s.id,
                s.name,
                s.url,
                s.avatar,
                s.type,
                s.description,
                s.createdBy,
                s.createdAt
                )
              FROM Source s
              WHERE
                ( :keyword = '' OR
                LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(s.url) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                )
                AND s.deleted = false
                AND (:type IS NULL OR s.type IN :type)
              ORDER BY s.createdAt DESC
        """)
    Page<SourceFilterResponse> filter(
            @Param("keyword") String keyword,
            @Param("type") List<Integer> type,
            Pageable pageable
    );

    @Query("""
        SELECT s
        FROM Source s
        WHERE s.deleted = false
        ORDER BY s.createdAt DESC
        """)
    List<Source> findAllNotDeleted();
}

