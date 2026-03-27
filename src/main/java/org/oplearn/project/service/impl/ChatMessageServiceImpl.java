package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.chat.ChatMessage;
import org.oplearn.project.exception.base.chat.ChatMessageNotFoundException;
import org.oplearn.project.exception.base.chat.NotMessageSenderException;
import org.oplearn.project.repository.ChatMessageRepository;
import org.oplearn.project.service.ChatMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    @Override
    public ChatMessage save(Long groupId, Long senderId, String content, String messageType) {
        ChatMessage message = ChatMessage.builder()
                .groupId(groupId)
                .senderId(senderId)
                .content(content)
                .messageType(messageType)
                .deleted(false)
                .build();
        return chatMessageRepository.save(message);
    }

    @Override
    public ChatMessage getById(Long messageId) {
        return chatMessageRepository.findByIdAndDeletedFalse(messageId)
                .orElseThrow(ChatMessageNotFoundException::new);
    }

    @Transactional
    @Override
    public ChatMessage recallMessage(Long messageId, Long requesterId) {
        ChatMessage message = chatMessageRepository.findByIdAndDeletedFalse(messageId)
                .orElseThrow(ChatMessageNotFoundException::new);
        if (!message.getSenderId().equals(requesterId)) {
            throw new NotMessageSenderException();
        }
        chatMessageRepository.recallById(messageId);
        message.setRecalled(true);
        return message;
    }

    @Override
    public Page<ChatMessage> getMessages(Long groupId, int page, int size) {
        return chatMessageRepository.findAllByGroupIdAndDeletedFalseOrderByCreatedAtDesc(
                groupId, PageRequest.of(page, size));
    }

    @Override
    public List<Long> getMessageIdsByGroupId(Long groupId) {
        return chatMessageRepository.findIdsByGroupId(groupId);
    }
}
