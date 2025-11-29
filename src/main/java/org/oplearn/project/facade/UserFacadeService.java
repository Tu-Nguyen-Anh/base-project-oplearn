package org.oplearn.project.facade;

import org.oplearn.project.dto.request.ChangePasswordRequest;
import org.oplearn.project.dto.request.UserFilterRequest;
import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.user.UserFilterResponse;
import org.oplearn.project.dto.response.user.UserHistoryResponse;
import org.oplearn.project.dto.response.user.UserResponse;

public interface UserFacadeService {
    UserResponse create(UserRequest request);

    UserResponse update(UserRequest request, Long id);

    void resetPassword(Long id);

    void delete(Long id);

    PageResponse<UserHistoryResponse> getHistories(Long userId, int page, int size);

    void changePassword(Long id, ChangePasswordRequest request);

    PageResponse<UserFilterResponse> filter(UserFilterRequest request);
}

