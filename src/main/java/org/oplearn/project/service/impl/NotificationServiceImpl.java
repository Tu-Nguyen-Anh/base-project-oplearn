package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.notification.NotificationResponse;
import org.oplearn.project.dto.response.notification.UnreadCountResponse;
import org.oplearn.project.entity.notification.Notification;
import org.oplearn.project.repository.NotificationRepository;
import org.oplearn.project.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.oplearn.project.constanst.OpLearnConstants.NotificationConstants.NOTIFICATION_TOPIC;
import static org.oplearn.project.constanst.OpLearnConstants.NotificationType.MENTION_IN_COMMENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @Override
    public void createMentionNotification(Long recipientUserId, Long senderUserId, String senderFullName,
                                          Long articleId, Long commentId) {
        log.info("=== Start createMentionNotification");
        log.debug("(createMentionNotification) recipientUserId: {}, senderUserId: {}", recipientUserId, senderUserId);

        Notification notification = Notification.builder()
                .recipientUserId(recipientUserId)
                .senderUserId(senderUserId)
                .senderFullName(senderFullName)
                .type(MENTION_IN_COMMENT)
                .message(senderFullName + " đã nhắc đến bạn trong một bình luận")
                .articleId(articleId)
                .commentId(commentId)
                .isRead(false)
                .build();

        Notification saved = repository.save(notification);

        NotificationResponse payload = new NotificationResponse(
                saved.getId(), senderUserId, senderFullName,
                MENTION_IN_COMMENT,
                saved.getMessage(),
                articleId, commentId,
                false, saved.getCreatedAt()
        );
        String topic = String.format(NOTIFICATION_TOPIC, recipientUserId);
        messagingTemplate.convertAndSend(topic, payload);
        log.debug("(createMentionNotification) pushed WebSocket notification to topic: {}", topic);
    }

    @Override
    public PageResponse<NotificationResponse> getNotifications(Long userId, int page, int size) {
        log.info("=== Start getNotifications");
        log.debug("(getNotifications) userId: {}, page: {}, size: {}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationResponse> result = repository.findByRecipientUserId(userId, pageable);

        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }

    @Override
    public UnreadCountResponse getUnreadCount(Long userId) {
        log.info("=== Start getUnreadCount");
        log.debug("(getUnreadCount) userId: {}", userId);

        long count = repository.countByRecipientUserIdAndIsReadFalse(userId);
        return new UnreadCountResponse(count);
    }

    @Transactional
    @Override
    public void markAsRead(Long notificationId, Long userId) {
        log.info("=== Start markAsRead");
        log.debug("(markAsRead) notificationId: {}, userId: {}", notificationId, userId);

        repository.markAsRead(notificationId, userId);
    }

    @Transactional
    @Override
    public void markAllAsRead(Long userId) {
        log.info("=== Start markAllAsRead");
        log.debug("(markAllAsRead) userId: {}", userId);

        repository.markAllAsRead(userId);
    }
}
