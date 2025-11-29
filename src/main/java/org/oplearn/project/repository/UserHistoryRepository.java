package org.oplearn.project.repository;


import org.oplearn.project.dto.response.user.UserHistoryResponse;
import org.oplearn.project.entity.user.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserHistoryRepository extends BaseRepository<UserHistory> {

    @Query("""
            SELECT distinct new org.oplearn.project.dto.response.user.UserHistoryResponse (
                uh.id,
                uh.message,
                uh.createdAt,
                uh.createdBy,
                u.fullName
            )
            FROM UserHistory uh
            LEFT JOIN User u ON uh.createdBy = u.username AND u.deleted = false
            WHERE uh.userId = :userId
            ORDER BY uh.createdAt DESC
            """)
    Page<UserHistoryResponse> findByUserId(@Param("userId") Long userId, Pageable pageable);


}
