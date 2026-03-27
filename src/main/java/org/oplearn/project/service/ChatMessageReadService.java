package org.oplearn.project.service;

import org.oplearn.project.dto.response.chat.ReaderResponse;

import java.util.List;
import java.util.Map;

public interface ChatMessageReadService {
    long markAllAsRead(Long groupId, Long userId);

    List<ReaderResponse> getReaders(Long messageId);

    Map<Long, List<ReaderResponse>> getReadersForMessages(List<Long> messageIds);
}
