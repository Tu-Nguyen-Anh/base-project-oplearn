package org.oplearn.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.UserResponse;
import org.oplearn.project.entity.User;
import org.oplearn.project.exception.base.UserNotFoundException;
import org.oplearn.project.repository.UserRepository;
import org.oplearn.project.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository repository;

  public UserServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserResponse create(UserRequest request) {

    User user = new User(
          request.getUsername(),
          request.getPassword(),
          request.getName(),
          request.getEmail(),
          request.getAddressId()
    );
    user = repository.save(user);
    return new UserResponse(
          user.getId(),
          user.getUsername(),
          user.getName(),
          user.getEmail(),
          request.getAddressId()
    );
  }

  @Override
  public List<UserResponse> list(String keyword, int size, int page, boolean isAll) {
    Pageable pageable = PageRequest.of(page, size);
    return isAll ?
          repository.findAllUsers() : repository.search(keyword, pageable);
  }

  @Override
  public UserResponse detail(String id) {
    UserResponse response = repository.findUserById(id);
    if (response == null) {
      throw new UserNotFoundException();
    }
    return response;
  }

  @Override
  public UserResponse update(UserRequest request, String id) {
    User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
    this.setValueForUpdate(request, user);
    user = repository.save(user);
    return new UserResponse(
          user.getUsername(),
          user.getPassword(),
          user.getName(),
          user.getEmail(),
          user.getAddressId()
    );

  }
//@Override
//public UserResponse update(UserRequest request, String id) {
//  repository.findById(id).orElseThrow(UserNotFoundException::new);
//  return repository.update(id, request.getUsername(), request.getPassword(), request.getName(), request.getEmail());
//
//}

  private void setValueForUpdate(UserRequest request, User user) {
    user.setAddressId(request.getAddressId());
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());
    user.setEmail(request.getEmail());
    user.setName(request.getName());
  }

//  @Override
//  public void delete(String id) {
//    this.detail(id);
//    log.info("(delete) id: {}", id);
//    repository.deleteById(id);
//  }
  @Override
  public void delete(String id) {
    this.detail(id);
    log.info("(delete) id: {}", id);
    repository.deleteUser(id);
  }

}
