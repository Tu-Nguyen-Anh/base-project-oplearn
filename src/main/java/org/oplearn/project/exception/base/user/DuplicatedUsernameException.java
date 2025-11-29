package org.oplearn.project.exception.base.user;

import org.oplearn.project.exception.base.ConflictException;

public class DuplicatedUsernameException extends ConflictException {
  public DuplicatedUsernameException() {
    super("com.cyai.soar.exception.base.user.DuplicatedUsernameException");
  }
}
