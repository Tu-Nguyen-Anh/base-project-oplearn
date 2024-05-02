package org.oplearn.project.service;

import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.UserResponse;

import java.util.List;

public interface UserService {
  UserResponse create(UserRequest request);

  UserResponse update(UserRequest request, String id);

  void delete(String id);
  List<UserResponse> list(String keyword, int size, int page, boolean isAll);

  UserResponse detail(String id);
}
