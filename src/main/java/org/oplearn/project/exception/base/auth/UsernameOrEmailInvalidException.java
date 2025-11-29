package org.oplearn.project.exception.base.auth;


import org.oplearn.project.exception.base.BadRequestException;

public class UsernameOrEmailInvalidException extends BadRequestException {
  public UsernameOrEmailInvalidException() {
    super("com.cyai.soar.exception.authenticate.UsernameOrEmailInvalidException");
  }
}
