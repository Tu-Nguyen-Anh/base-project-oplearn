package org.oplearn.project.service;

import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.notification.NotificationResponse;
import org.oplearn.project.dto.response.notification.UnreadCountResponse;

public interface NotificationService {
    void createMentionNotification(Long recipientUserId, Long senderUserId, String senderFullName,
                                   Long articleId, Long commentId);

    PageResponse<NotificationResponse> getNotifications(Long userId, int page, int size);

    UnreadCountResponse getUnreadCount(Long userId);

    void markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);
}
