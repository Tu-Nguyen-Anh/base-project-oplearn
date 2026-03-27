package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.BadRequestException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.NOT_MESSAGE_SENDER;

public class NotMessageSenderException extends BadRequestException {
    public NotMessageSenderException() {
        super(NOT_MESSAGE_SENDER);
    }
}
