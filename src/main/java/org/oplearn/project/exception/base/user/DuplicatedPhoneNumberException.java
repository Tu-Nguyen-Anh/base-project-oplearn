package org.oplearn.project.exception.base.user;

import org.oplearn.project.exception.base.ConflictException;

public class DuplicatedPhoneNumberException extends ConflictException {
  public DuplicatedPhoneNumberException(){
    super("com.cyai.soar.exception.base.user.DuplicatedPhoneNumberException");
  }
}
