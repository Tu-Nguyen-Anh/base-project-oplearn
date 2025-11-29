package org.oplearn.project.exception.base;

import java.util.HashMap;
import java.util.Map;


public class ForbiddenException extends BaseException{
  private static final String DEFAULT_CODE = "com.cyai.soar.core.exception.base.ForbiddenException";

  public ForbiddenException(String id, String objectName) {
    super(DEFAULT_CODE, "Forbidden", 403, createParams(id, objectName));
  }

  public ForbiddenException() {
    super(DEFAULT_CODE, "Forbidden", 403, null);
  }

  public ForbiddenException(String code) {
    super(code, "", 403, null);
  }

  private static Map<String, String> createParams(String id, String objectName) {
    Map<String, String> params = new HashMap<>();
    params.put("id", id);
    params.put("objectName", objectName);
    return params;
  }
}
