package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.BadRequestException;

public class PasswordWasUsedException extends BadRequestException {
  public PasswordWasUsedException(){
    super("com.cyai.soar.exception.user.PasswordWasUsedException");
  }
}
