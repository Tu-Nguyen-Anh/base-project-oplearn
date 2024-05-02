package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.UserResponse;
import org.oplearn.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constanst.OpLearnConstants.VariableConstant.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
  private final UserService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<UserResponse> create(
        @RequestBody UserRequest request
  ) {
    log.info("(create)request: {}", request);
    return ResponseGeneral.ofCreated(
          "success",
          service.create(request));
  }

  @PutMapping("{id}")
  public ResponseGeneral<UserResponse> update(
        @RequestBody UserRequest request,
        @PathVariable String id
  ) {
    log.info("(create)request: {}", request);
    return ResponseGeneral.ofSuccess(
          "success", service.update(request, id));
  }

  @GetMapping
  public ResponseGeneral<List<UserResponse>> list(
        @RequestParam(name = PARAM_KEYWORD, required = false) String keyword,
        @RequestParam(name = PARAM_SIZE, defaultValue = SIZE_DEFAULT) int size,
        @RequestParam(name = PARAM_PAGE, defaultValue = PAGE_DEFAULT) int page,
        @RequestParam(name = PARAM_ALL, defaultValue = IS_ALL_DEFAULT, required = false) boolean isAll) {
    log.info("(list) keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);
    return ResponseGeneral.ofSuccess("Success", service.list(keyword, size, page, isAll));
  }

  @GetMapping("{id}")
  public ResponseGeneral<UserResponse> detail(
        @RequestParam String id
  ) {
    return ResponseGeneral.ofSuccess("Success", service.detail(id));
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable String id
  ) {
    log.info("(delete) id: {}", id);
    service.delete(id);
    return ResponseGeneral.ofSuccess("Success");
  }
}
