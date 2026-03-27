package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.NotFoundException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.CHAT_GROUP_NOT_FOUND;

public class ChatGroupNotFoundException extends NotFoundException {
    public ChatGroupNotFoundException() {
        super(CHAT_GROUP_NOT_FOUND);
    }
}
