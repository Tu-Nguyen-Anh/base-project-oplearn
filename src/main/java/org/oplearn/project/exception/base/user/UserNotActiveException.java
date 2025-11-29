package org.oplearn.project.exception.base.user;

import org.oplearn.project.exception.base.BadRequestException;

public class UserNotActiveException extends BadRequestException {
    public UserNotActiveException() {
        super("com.cyai.soar.exception.user.UserNotActiveException");
    }
}
