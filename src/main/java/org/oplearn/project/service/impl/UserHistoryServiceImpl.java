package org.oplearn.project.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.user.UserHistoryResponse;
import org.oplearn.project.entity.user.UserHistory;
import org.oplearn.project.repository.UserHistoryRepository;
import org.oplearn.project.service.UserHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserHistoryServiceImpl implements UserHistoryService {
  private final UserHistoryRepository repository;

  @Override
  public PageResponse<UserHistoryResponse> filter(Long userId, int page, int size) {
    log.info("=== Start filter user history");
    log.debug("userId : {}", userId);

    Pageable pageable = PageRequest.of(page, size);

    Page<UserHistoryResponse> userHistoryResponses = repository.findByUserId(userId, pageable);

    return PageResponse.of(userHistoryResponses.getContent(), (int) userHistoryResponses.getTotalElements());
  }

  @Override
  public void saveHistory(Long userId, String message) {
    log.info("=== Start save history");
    log.debug("(saveHistory) user: {}, message: {}", userId, message);

    UserHistory history = UserHistory.builder()
          .userId(userId)
          .message(message)
          .build();

    repository.save(history);
  }
}
