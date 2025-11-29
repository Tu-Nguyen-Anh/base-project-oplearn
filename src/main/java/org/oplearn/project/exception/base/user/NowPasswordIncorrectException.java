package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.BadRequestException;

public class NowPasswordIncorrectException extends BadRequestException {
  public NowPasswordIncorrectException() {
    super("com.cyai.soar.exception.authenticate.NowPasswordIncorrectException");
  }
}
