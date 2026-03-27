package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.BadRequestException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.NOT_GROUP_MEMBER;

public class NotGroupMemberException extends BadRequestException {
    public NotGroupMemberException() {
        super(NOT_GROUP_MEMBER);
    }
}
