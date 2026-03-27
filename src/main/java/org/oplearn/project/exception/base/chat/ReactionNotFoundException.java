package org.oplearn.project.exception.base.chat;

import org.oplearn.project.exception.base.NotFoundException;

import static org.oplearn.project.constanst.OpLearnConstants.ChatException.REACTION_NOT_FOUND;

public class ReactionNotFoundException extends NotFoundException {
    public ReactionNotFoundException() {
        super(REACTION_NOT_FOUND);
    }
}
