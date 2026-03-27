package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.notification.NotificationResponse;
import org.oplearn.project.dto.response.notification.UnreadCountResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.NotificationService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final MessageService messageService;

    @GetMapping
    public ResponseGeneral<PageResponse<NotificationResponse>> getNotifications(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getNotifications) page: {}, size: {}", page, size);

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                notificationService.getNotifications(currentUser.getId(), page, size)
        );
    }

    @GetMapping("/unread-count")
    public ResponseGeneral<UnreadCountResponse> getUnreadCount(
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getUnreadCount)");

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                notificationService.getUnreadCount(currentUser.getId())
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseGeneral<Void> markAsRead(
            @PathVariable Long notificationId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(markAsRead) notificationId: {}", notificationId);

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        notificationService.markAsRead(notificationId, currentUser.getId());

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @PatchMapping("/read-all")
    public ResponseGeneral<Void> markAllAsRead(
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(markAllAsRead)");

        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        notificationService.markAllAsRead(currentUser.getId());

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }
}
