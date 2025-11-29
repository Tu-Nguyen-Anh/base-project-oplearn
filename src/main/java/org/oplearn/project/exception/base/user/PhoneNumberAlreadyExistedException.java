package org.oplearn.project.exception.base.user;

import org.oplearn.project.exception.base.ConflictException;

public class PhoneNumberAlreadyExistedException extends ConflictException {
  public PhoneNumberAlreadyExistedException() {
    super("com.cyai.soar.service.mongo.exception.user.PhoneNumberAlreadyExistedException");
  }
}
