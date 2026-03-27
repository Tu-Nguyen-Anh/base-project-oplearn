package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.BadRequestException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.CANNOT_LEAVE_GROUP;

public class CannotLeaveGroupException extends BadRequestException {
    public CannotLeaveGroupException() {
        super(CANNOT_LEAVE_GROUP);
    }
}
