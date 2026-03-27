package org.oplearn.project.repository;

import org.oplearn.project.dto.response.notification.NotificationResponse;
import org.oplearn.project.entity.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends BaseRepository<Notification> {

    @Query("""
            SELECT new org.oplearn.project.dto.response.notification.NotificationResponse(
                n.id,
                n.senderUserId,
                n.senderFullName,
                n.type,
                n.message,
                n.articleId,
                n.commentId,
                n.isRead,
                n.createdAt
            )
            FROM Notification n
            WHERE n.recipientUserId = :userId
            ORDER BY n.createdAt DESC
            """)
    Page<NotificationResponse> findByRecipientUserId(@Param("userId") Long userId, Pageable pageable);

    long countByRecipientUserIdAndIsReadFalse(Long recipientUserId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id AND n.recipientUserId = :userId")
    void markAsRead(@Param("id") Long id, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipientUserId = :userId AND n.isRead = false")
    void markAllAsRead(@Param("userId") Long userId);
}
