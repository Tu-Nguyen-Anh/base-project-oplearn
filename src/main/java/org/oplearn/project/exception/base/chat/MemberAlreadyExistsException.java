package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.ConflictException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.MEMBER_ALREADY_EXISTS;

public class MemberAlreadyExistsException extends ConflictException {
    public MemberAlreadyExistsException() {
        super(MEMBER_ALREADY_EXISTS);
    }
}
