package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.BadRequestException;

public class UsernameNotNullException extends BadRequestException {
  public UsernameNotNullException() {
    super("com.cyai.soar.exception.authenticate.UsernameNotNullException");
  }
}
