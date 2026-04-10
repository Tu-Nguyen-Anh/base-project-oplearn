package org.oplearn.project.exception.base.feedback;

import org.oplearn.project.exception.base.ForbiddenException;

import static org.oplearn.project.constanst.OpLearnConstants.FeedbackException.FEEDBACK_NOT_OWNER;

public class FeedbackNotOwnerException extends ForbiddenException {
    public FeedbackNotOwnerException() {
        super(FEEDBACK_NOT_OWNER);
    }
}
