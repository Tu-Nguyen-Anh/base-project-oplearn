package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.chat.ReactionResponse;
import org.oplearn.project.entity.chat.ChatMessageReaction;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.exception.base.chat.ReactionAlreadyExistsException;
import org.oplearn.project.exception.base.chat.ReactionNotFoundException;
import org.oplearn.project.repository.ChatMessageReactionRepository;
import org.oplearn.project.service.ChatMessageReactionService;
import org.oplearn.project.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageReactionServiceImpl implements ChatMessageReactionService {

    private final ChatMessageReactionRepository chatMessageReactionRepository;
    private final UserService userService;

    @Transactional
    @Override
    public List<ReactionResponse> addReaction(Long messageId, Long userId, String emoji) {
        if (chatMessageReactionRepository.existsByMessageIdAndUserIdAndEmoji(messageId, userId, emoji)) {
            throw new ReactionAlreadyExistsException();
        }
        chatMessageReactionRepository.save(ChatMessageReaction.builder()
                .messageId(messageId)
                .userId(userId)
                .emoji(emoji)
                .createdAt(System.currentTimeMillis())
                .build());
        return getReactions(messageId, userId);
    }

    @Transactional
    @Override
    public List<ReactionResponse> removeReaction(Long messageId, Long userId, String emoji) {
        ChatMessageReaction reaction = chatMessageReactionRepository
                .findByMessageIdAndUserIdAndEmoji(messageId, userId, emoji)
                .orElseThrow(ReactionNotFoundException::new);
        chatMessageReactionRepository.delete(reaction);
        return getReactions(messageId, userId);
    }

    @Override
    public List<ReactionResponse> getReactions(Long messageId, Long currentUserId) {
        List<ChatMessageReaction> reactions = chatMessageReactionRepository.findAllByMessageId(messageId);
        return buildReactionResponses(reactions, currentUserId);
    }

    @Override
    public Map<Long, List<ReactionResponse>> getReactionsForMessages(List<Long> messageIds, Long currentUserId) {
        if (messageIds.isEmpty()) {
            return Map.of();
        }

        List<ChatMessageReaction> reactions = chatMessageReactionRepository.findAllByMessageIdIn(messageIds);

        Map<Long, User> userCache = reactions.stream()
                .map(ChatMessageReaction::getUserId)
                .distinct()
                .collect(Collectors.toMap(id -> id, userService::getById));

        Map<Long, List<ChatMessageReaction>> grouped = reactions.stream()
                .collect(Collectors.groupingBy(ChatMessageReaction::getMessageId));

        Map<Long, List<ReactionResponse>> result = new LinkedHashMap<>();
        for (Long messageId : messageIds) {
            List<ChatMessageReaction> msgReactions = grouped.getOrDefault(messageId, List.of());
            result.put(messageId, buildReactionResponsesWithCache(msgReactions, currentUserId, userCache));
        }
        return result;
    }

    private List<ReactionResponse> buildReactionResponses(List<ChatMessageReaction> reactions, Long currentUserId) {
        Map<Long, User> userCache = reactions.stream()
                .map(ChatMessageReaction::getUserId)
                .distinct()
                .collect(Collectors.toMap(id -> id, userService::getById));
        return buildReactionResponsesWithCache(reactions, currentUserId, userCache);
    }

    private List<ReactionResponse> buildReactionResponsesWithCache(
            List<ChatMessageReaction> reactions, Long currentUserId, Map<Long, User> userCache) {

        Map<String, List<ChatMessageReaction>> byEmoji = reactions.stream()
                .collect(Collectors.groupingBy(ChatMessageReaction::getEmoji, LinkedHashMap::new, Collectors.toList()));

        List<ReactionResponse> result = new ArrayList<>();
        for (Map.Entry<String, List<ChatMessageReaction>> entry : byEmoji.entrySet()) {
            String emoji = entry.getKey();
            List<ChatMessageReaction> emojiReactions = entry.getValue();

            boolean reactedByMe = emojiReactions.stream()
                    .anyMatch(r -> r.getUserId().equals(currentUserId));

            List<ReactionResponse.ReactorInfo> reactors = emojiReactions.stream()
                    .map(r -> {
                        User user = userCache.get(r.getUserId());
                        return ReactionResponse.ReactorInfo.builder()
                                .userId(user.getId())
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .avatar(user.getAvatar())
                                .build();
                    })
                    .toList();

            result.add(ReactionResponse.builder()
                    .emoji(emoji)
                    .count(emojiReactions.size())
                    .reactedByMe(reactedByMe)
                    .reactors(reactors)
                    .build());
        }
        return result;
    }
}
