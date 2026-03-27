package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.BadRequestException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.NOT_GROUP_ADMIN;

public class NotGroupAdminException extends BadRequestException {
    public NotGroupAdminException() {
        super(NOT_GROUP_ADMIN);
    }
}
