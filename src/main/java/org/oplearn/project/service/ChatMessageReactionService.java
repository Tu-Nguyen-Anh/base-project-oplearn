package org.oplearn.project.service;

import org.oplearn.project.dto.response.chat.ReactionResponse;

import java.util.List;
import java.util.Map;

public interface ChatMessageReactionService {
    List<ReactionResponse> addReaction(Long messageId, Long userId, String emoji);

    List<ReactionResponse> removeReaction(Long messageId, Long userId, String emoji);

    List<ReactionResponse> getReactions(Long messageId, Long currentUserId);

    Map<Long, List<ReactionResponse>> getReactionsForMessages(List<Long> messageIds, Long currentUserId);
}
