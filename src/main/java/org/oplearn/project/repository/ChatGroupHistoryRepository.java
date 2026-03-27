package org.oplearn.project.repository;

import org.oplearn.project.dto.response.chat.ChatGroupHistoryResponse;
import org.oplearn.project.entity.chat.ChatGroupHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatGroupHistoryRepository extends BaseRepository<ChatGroupHistory> {

    @Query("""
            SELECT new org.oplearn.project.dto.response.chat.ChatGroupHistoryResponse(
                h.id,
                h.groupId,
                h.action,
                h.message,
                h.createdAt,
                h.createdBy,
                u.fullName
            )
            FROM ChatGroupHistory h
            LEFT JOIN User u ON h.createdBy = u.username AND u.deleted = false
            WHERE h.groupId = :groupId
            ORDER BY h.createdAt DESC
            """)
    Page<ChatGroupHistoryResponse> findByGroupId(@Param("groupId") Long groupId, Pageable pageable);
}
