package org.oplearn.project.exception.base;

public class UnauthorizedException extends BaseException {

  private static final String DEFAULT_CODE = "com.cyai.soar.core.exception.base.UnauthorizedException";

  public UnauthorizedException() {
    super(DEFAULT_CODE, "Unauthorized", 401, null);
  }

  public UnauthorizedException(String code) {
    super(code, "", 401, null);
  }
}
