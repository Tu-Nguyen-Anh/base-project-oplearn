package org.oplearn.project.service;

import org.oplearn.project.entity.chat.ChatMessage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatMessageService {
    ChatMessage save(Long groupId, Long senderId, String content, String messageType);

    ChatMessage getById(Long messageId);

    ChatMessage recallMessage(Long messageId, Long requesterId);

    Page<ChatMessage> getMessages(Long groupId, int page, int size);

    List<Long> getMessageIdsByGroupId(Long groupId);
}
