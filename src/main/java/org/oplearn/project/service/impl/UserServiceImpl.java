package org.oplearn.project.service.impl;

import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.UserResponse;
import org.oplearn.project.service.UserService;
import org.springframework.data.domain.Page;

public class UserServiceImpl implements UserService {


  @Override
  public UserResponse create(UserRequest request) {
    return null;
  }

  @Override
  public UserResponse update(UserRequest request, String id) {
    return null;
  }

  @Override
  public void delete(String id) {

  }

  @Override
  public Page<UserResponse> list(String keyword, int size, int page, boolean isAll) {
    return null;
  }
}
