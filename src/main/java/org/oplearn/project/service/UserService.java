package org.oplearn.project.service;

import org.oplearn.project.dto.request.UserFilterRequest;
import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.user.UserFilterResponse;
import org.oplearn.project.dto.response.user.UserMentionResponse;
import org.oplearn.project.dto.response.user.UserResponse;
import org.oplearn.project.entity.user.User;

public interface UserService {
    User getById(Long userId);

    User findByUsername(String userName);

    UserResponse create(UserRequest request);

    UserResponse update(UserRequest request, Long id);

    void delete(Long id);

    void checkUsernameExists(String username);

    void checkPhoneNumberExists(String phoneNumber);

    void checkEmailExists(String email);

    void resetPassword(Long id, String randomPassword);

    void save(User users);

    PageResponse<UserFilterResponse> filter(UserFilterRequest request);

    UserResponse detail(Long id);

    public User checkExistById(Long id);

    PageResponse<UserMentionResponse> searchForMention(String keyword, int page, int size);
}
