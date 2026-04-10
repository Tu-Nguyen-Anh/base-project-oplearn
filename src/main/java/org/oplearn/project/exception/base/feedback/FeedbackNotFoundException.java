package org.oplearn.project.exception.base.feedback;

import org.oplearn.project.exception.base.NotFoundException;

import static org.oplearn.project.constanst.OpLearnConstants.FeedbackException.FEEDBACK_NOT_FOUND;

public class FeedbackNotFoundException extends NotFoundException {
    public FeedbackNotFoundException() {
        super(FEEDBACK_NOT_FOUND);
    }
}
