package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.chat.ChatGroupHistoryResponse;
import org.oplearn.project.entity.chat.ChatGroupHistory;
import org.oplearn.project.repository.ChatGroupHistoryRepository;
import org.oplearn.project.service.ChatGroupHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGroupHistoryServiceImpl implements ChatGroupHistoryService {

    private final ChatGroupHistoryRepository chatGroupHistoryRepository;

    @Override
    public void saveHistory(Long groupId, String action, String message) {
        log.debug("(saveHistory) groupId: {}, action: {}, message: {}", groupId, action, message);
        ChatGroupHistory history = ChatGroupHistory.builder()
                .groupId(groupId)
                .action(action)
                .message(message)
                .build();
        chatGroupHistoryRepository.save(history);
    }

    @Override
    public PageResponse<ChatGroupHistoryResponse> getHistory(Long groupId, int page, int size) {
        log.info("(getHistory) groupId: {}, page: {}, size: {}", groupId, page, size);
        Page<ChatGroupHistoryResponse> result =
                chatGroupHistoryRepository.findByGroupId(groupId, PageRequest.of(page, size));
        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }
}
