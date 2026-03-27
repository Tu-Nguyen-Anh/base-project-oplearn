package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.chat.ReaderResponse;
import org.oplearn.project.entity.chat.ChatMessageRead;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.repository.ChatMessageReadRepository;
import org.oplearn.project.service.ChatMessageReadService;
import org.oplearn.project.service.ChatMessageService;
import org.oplearn.project.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageReadServiceImpl implements ChatMessageReadService {

    private final ChatMessageReadRepository chatMessageReadRepository;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @Transactional
    @Override
    public long markAllAsRead(Long groupId, Long userId) {
        List<Long> allMessageIds = chatMessageService.getMessageIdsByGroupId(groupId);
        if (allMessageIds.isEmpty()) {
            return 0L;
        }

        long now = System.currentTimeMillis();
        List<ChatMessageRead> toSave = new ArrayList<>();

        for (Long messageId : allMessageIds) {
            if (!chatMessageReadRepository.existsByMessageIdAndUserId(messageId, userId)) {
                toSave.add(ChatMessageRead.builder()
                        .messageId(messageId)
                        .userId(userId)
                        .readAt(now)
                        .build());
            }
        }

        if (!toSave.isEmpty()) {
            chatMessageReadRepository.saveAll(toSave);
        }

        return allMessageIds.stream()
                .filter(id -> toSave.stream().anyMatch(r -> r.getMessageId().equals(id))
                        || chatMessageReadRepository.existsByMessageIdAndUserId(id, userId))
                .count();
    }

    @Override
    public List<ReaderResponse> getReaders(Long messageId) {
        return chatMessageReadRepository.findAllByMessageId(messageId)
                .stream()
                .map(read -> {
                    User user = userService.getById(read.getUserId());
                    return ReaderResponse.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .fullName(user.getFullName())
                            .avatar(user.getAvatar())
                            .readAt(read.getReadAt())
                            .build();
                })
                .toList();
    }

    @Override
    public Map<Long, List<ReaderResponse>> getReadersForMessages(List<Long> messageIds) {
        if (messageIds.isEmpty()) {
            return Map.of();
        }

        List<ChatMessageRead> reads = chatMessageReadRepository.findAllByMessageIdIn(messageIds);

        Map<Long, User> userCache = reads.stream()
                .map(ChatMessageRead::getUserId)
                .distinct()
                .collect(Collectors.toMap(id -> id, userService::getById));

        return reads.stream().collect(Collectors.groupingBy(
                ChatMessageRead::getMessageId,
                Collectors.mapping(read -> {
                    User user = userCache.get(read.getUserId());
                    return ReaderResponse.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .fullName(user.getFullName())
                            .avatar(user.getAvatar())
                            .readAt(read.getReadAt())
                            .build();
                }, Collectors.toList())
        ));
    }
}
