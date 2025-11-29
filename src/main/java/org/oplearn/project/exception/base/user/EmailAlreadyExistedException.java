package org.oplearn.project.exception.base.user;


import org.oplearn.project.exception.base.ConflictException;

public class EmailAlreadyExistedException extends ConflictException {
  public EmailAlreadyExistedException() {
    super("com.cyai.soar.service.mongo.exception.user.EmailAlreadyExistedException");
  }
}
