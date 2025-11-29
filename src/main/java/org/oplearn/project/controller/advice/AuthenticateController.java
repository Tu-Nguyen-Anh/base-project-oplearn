package org.oplearn.project.controller.advice;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.authenticate.LoginRequest;
import org.oplearn.project.dto.request.authenticate.RefreshTokenRequest;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.authenticate.LoginResponse;
import org.oplearn.project.facade.AuthenticateFacadeService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticateController {
  private final AuthenticateFacadeService authenticateFacadeService;
  private final MessageService messageService;

  @PostMapping("/login")
  public ResponseGeneral<LoginResponse> login(
        @RequestBody LoginRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language

  ) {
    log.info("=== Start login ");
    log.debug("(login) request: {}", request);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          authenticateFacadeService.authenticate(request)
    );
  }

  @PostMapping("logout")
  public ResponseGeneral<Void> logout(
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("====> Start logout");
    log.debug("====> (logout) ");

    authenticateFacadeService.logout();

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language)
    );
  }

  @PostMapping("/refresh")
  public ResponseGeneral<LoginResponse> refresh(
        @RequestBody RefreshTokenRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language

  ) {
    log.info("====> Start refresh");
    log.debug("====> (refresh) request: {}", request);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(SUCCESS, language),
          authenticateFacadeService.refreshToken(request));
  }
}
