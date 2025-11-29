package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.BadRequestException;

public class PasswordIncorrectException extends BadRequestException {
  private static final String DEFAULT_CODE = "com.cyai.soar.exception.authenticate.PasswordIncorrectException";

  public PasswordIncorrectException() {
    super(DEFAULT_CODE);
  }
}
