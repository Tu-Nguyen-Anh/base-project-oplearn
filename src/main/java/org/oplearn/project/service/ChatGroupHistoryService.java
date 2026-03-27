package org.oplearn.project.service;

import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.chat.ChatGroupHistoryResponse;

public interface ChatGroupHistoryService {
    void saveHistory(Long groupId, String action, String message);

    PageResponse<ChatGroupHistoryResponse> getHistory(Long groupId, int page, int size);
}
