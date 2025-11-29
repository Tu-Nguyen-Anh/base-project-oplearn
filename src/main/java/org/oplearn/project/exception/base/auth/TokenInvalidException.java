package org.oplearn.project.exception.base.auth;

import org.oplearn.project.exception.base.UnauthorizedException;

public class TokenInvalidException extends UnauthorizedException {
  public TokenInvalidException() {
    super("com.cyai.soar.exception.authenticate.TokenValidException");
  }
}
