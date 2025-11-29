package org.oplearn.project.utils;

import org.oplearn.project.exception.base.user.PasswordIncorrectException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderUtils {
  public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

  public static PasswordEncoder getPasswordEncoder() {
    return PASSWORD_ENCODER;
  }

  public static boolean equalPassword(String passwordRaw, String passwordEncrypted) {
    return getPasswordEncoder().matches(passwordRaw, passwordEncrypted);

  }

  public static void checkEqualPassword(String passwordRaw, String passwordEncrypted) {
    if (!getPasswordEncoder().matches(passwordRaw, passwordEncrypted)) {
      throw new PasswordIncorrectException();
    }
  }

}
