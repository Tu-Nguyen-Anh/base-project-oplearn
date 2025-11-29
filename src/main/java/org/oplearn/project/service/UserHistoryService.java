package org.oplearn.project.service;


import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.user.UserHistoryResponse;

public interface UserHistoryService {
    PageResponse<UserHistoryResponse> filter(Long userId, int page, int size);

    void saveHistory(Long userId, String message);
}
