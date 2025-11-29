package org.oplearn.project.facade;

import org.oplearn.project.dto.request.authenticate.LoginRequest;
import org.oplearn.project.dto.request.authenticate.RefreshTokenRequest;
import org.oplearn.project.dto.response.authenticate.LoginResponse;
import org.oplearn.project.entity.user.User;

public interface AuthenticateFacadeService {
  LoginResponse authenticate(LoginRequest request);

  LoginResponse refreshToken(RefreshTokenRequest request);

  void logout();

  void setUserToSecurityContext(User user);
}
