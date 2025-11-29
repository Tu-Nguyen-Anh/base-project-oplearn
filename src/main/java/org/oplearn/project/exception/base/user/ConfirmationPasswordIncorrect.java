package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.ConflictException;

public class ConfirmationPasswordIncorrect extends ConflictException {
  public ConfirmationPasswordIncorrect(){
    super("com.cyai.soar.exception.user.ConfirmationPasswordIncorrect");
  }
}
