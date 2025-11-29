package org.oplearn.project.security;


import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.exception.base.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

import static org.oplearn.project.constanst.OpLearnConstants.AuditorConstant.ANONYMOUS;


@Slf4j
public class UserAuthenticated {
  private UserAuthenticated() {}
  public static Optional<User> getCurrentUser() {
    var authenticate = SecurityContextHolder.getContext().getAuthentication();
    log.debug("=== authenticate: {}", authenticate);
    log.debug("=== principal: {}", authenticate.getPrincipal().toString());
    if (Objects.equals(authenticate.getPrincipal(), ANONYMOUS)) {
      log.error("(getCurrentUser) ===== Unauthorized");
      throw new UnauthorizedException();
    }

    return Optional.of(authenticate)
          .map(authentication -> (User) authenticate.getPrincipal());
  }

  public static User getCurrentUserThrowUnAuthorized() {
    Authentication authenticate = SecurityContextHolder.getContext().getAuthentication();
    log.debug("=== authenticate: {}", authenticate);

    if (authenticate == null || !authenticate.isAuthenticated()) {
      throw new UnauthorizedException();
    }

    log.debug("=== principal: {}", authenticate.getPrincipal().toString());

    return Optional.ofNullable(authenticate.getPrincipal())
          .filter(User.class::isInstance)
          .map(User.class::cast)
          .orElseThrow(UnauthorizedException::new);
  }
}
