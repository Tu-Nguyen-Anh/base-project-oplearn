package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.BadRequestException;

public class PasswordNotNullException extends BadRequestException {
  public PasswordNotNullException(){
    super("com.cyai.soar.exception.authenticate.PasswordNotNullException");
  }
}
