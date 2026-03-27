package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.ConflictException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.REACTION_ALREADY_EXISTS;

public class ReactionAlreadyExistsException extends ConflictException {
    public ReactionAlreadyExistsException() {
        super(REACTION_ALREADY_EXISTS);
    }
}
