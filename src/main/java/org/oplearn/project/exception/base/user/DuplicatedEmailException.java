package org.oplearn.project.exception.base.user;

import org.oplearn.project.exception.base.ConflictException;

public class DuplicatedEmailException extends ConflictException {
  public DuplicatedEmailException(){
    super("com.cyai.soar.exception.base.user.DuplicatedEmailException");
  }
}
