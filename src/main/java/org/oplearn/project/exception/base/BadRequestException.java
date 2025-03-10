package org.oplearn.project.exception.base;


import static org.oplearn.project.constanst.OpLearnConstants.MessageException.DEFAULT_CODE_BAD_REQUEST;
import static org.oplearn.project.constanst.OpLearnConstants.StatusException.BAD_REQUEST;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.*;
import static org.oplearn.project.constanst.OpLearnConstants.StatusException.NOT_FOUND;

public class BadRequestException extends BaseException {


  public BadRequestException() {
    super(DEFAULT_CODE_BAD_REQUEST, BAD_REQUEST_MESSAGE, BAD_REQUEST, null);
  }

  public BadRequestException(String code) {
    super(code, BAD_REQUEST_MESSAGE, BAD_REQUEST, null);
  }
}
