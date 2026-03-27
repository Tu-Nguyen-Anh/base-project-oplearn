package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.NotFoundException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.CHAT_MESSAGE_NOT_FOUND;

public class ChatMessageNotFoundException extends NotFoundException {
    public ChatMessageNotFoundException() {
        super(CHAT_MESSAGE_NOT_FOUND);
    }
}
