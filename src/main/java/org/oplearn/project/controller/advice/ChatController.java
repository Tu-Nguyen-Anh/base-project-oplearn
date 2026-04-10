package org.oplearn.project.controller.advice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.chat.SendMessageRequest;
import org.oplearn.project.dto.response.chat.ChatMessageResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.exception.base.chat.NotGroupMemberException;
import org.oplearn.project.facade.ChatGroupFacadeService;
import org.oplearn.project.service.ChatGroupMemberService;
import org.oplearn.project.service.ChatMessageService;
import org.oplearn.project.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import java.security.Principal;

import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.CHAT_TOPIC;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatGroupMemberService chatGroupMemberService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{groupId}")
    public void sendMessage(
            @DestinationVariable Long groupId,
            @Payload @Valid SendMessageRequest request,
            Principal principal
    ) {
        if (principal == null) {
            log.warn("(sendMessage) Unauthenticated WebSocket message to groupId: {}", groupId);
            return;
        }

        User sender = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        log.info("(sendMessage) groupId: {}, sender: {}", groupId, sender.getUsername());

        if (!chatGroupMemberService.isMember(groupId, sender.getId())) {
            throw new NotGroupMemberException();
        }

        var message = chatMessageService.save(
                groupId, sender.getId(), request.getContent(), request.getMessageType());

        ChatMessageResponse response = ChatMessageResponse.builder()
                .id(message.getId())
                .groupId(message.getGroupId())
                .senderId(sender.getId())
                .senderUsername(sender.getUsername())
                .senderFullName(sender.getFullName())
                .senderAvatar(sender.getAvatar())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .createdAt(message.getCreatedAt())
                .build();

        messagingTemplate.convertAndSend(CHAT_TOPIC + groupId, response);
    }
}
