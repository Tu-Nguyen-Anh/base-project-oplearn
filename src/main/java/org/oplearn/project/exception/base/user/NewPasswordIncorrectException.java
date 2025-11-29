package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.BadRequestException;

public class NewPasswordIncorrectException extends BadRequestException {
  public NewPasswordIncorrectException() {
    super("com.cyai.soar.exception.authenticate.NewPasswordIncorrectException");
  }
}
