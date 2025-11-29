package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.ConflictException;

public class UsernameAlreadyExistedException extends ConflictException {
  public UsernameAlreadyExistedException() {
    super("com.cyai.soar.service.mongo.exception.user.UsernameAlreadyExistedException");
  }
}
