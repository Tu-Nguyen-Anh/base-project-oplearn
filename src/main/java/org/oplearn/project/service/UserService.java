package org.oplearn.project.service;

import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
  UserResponse create(UserRequest request);

  UserResponse update(UserRequest request, String id);

  void delete(String id);
  Page<UserResponse> list(String keyword, int size, int page, boolean isAll);
}
