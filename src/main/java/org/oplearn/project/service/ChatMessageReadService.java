package org.oplearn.project.service;

import org.oplearn.project.dto.response.chat.ReaderResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ChatMessageReadService {
    /**
     * Marks all messages in the group as read for the user.
     * Returns the ID of the last (highest ID) message marked, or empty if no messages.
     */
    Optional<Long> markAllAsRead(Long groupId, Long userId);

    List<ReaderResponse> getReaders(Long messageId);

    Map<Long, List<ReaderResponse>> getReadersForMessages(List<Long> messageIds);
}
