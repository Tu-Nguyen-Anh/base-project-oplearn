package org.oplearn.project.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.chat.PresenceEvent;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.service.ChatGroupService;
import org.oplearn.project.service.PresenceService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.PRESENCE_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final PresenceService presenceService;
    private final ChatGroupService chatGroupService;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @EventListener
    public void handleWebSocketConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        User user = extractUser(accessor);
        if (user == null) return;

        presenceService.setOnline(user.getId());
        log.info("(WebSocket connected) userId: {}, username: {}", user.getId(), user.getUsername());

        broadcastPresence(user, true);
    }

    @Async
    @EventListener
    public void handleWebSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        User user = extractUser(accessor);
        if (user == null) return;

        presenceService.setOffline(user.getId()); // lưu lastSeen vào Redis trước
        log.info("(WebSocket disconnected) userId: {}, username: {}", user.getId(), user.getUsername());

        broadcastPresence(user, false);
    }

    private User extractUser(StompHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        if (principal instanceof UsernamePasswordAuthenticationToken auth) {
            Object p = auth.getPrincipal();
            if (p instanceof User user) {
                return user;
            }
        }
        return null;
    }

    private void broadcastPresence(User user, boolean online) {
        PresenceEvent event;
        if (online) {
            event = PresenceEvent.online(user.getId(), user.getUsername(), user.getFullName(), user.getAvatar());
        } else {
            Long lastSeen = presenceService.getLastSeen(user.getId());
            event = PresenceEvent.offline(user.getId(), user.getUsername(), user.getFullName(), user.getAvatar(), lastSeen);
        }

        chatGroupService.getGroupsByUserId(user.getId()).forEach(group ->
                messagingTemplate.convertAndSend(
                        String.format(PRESENCE_TOPIC, group.getId()), event));
    }
}
