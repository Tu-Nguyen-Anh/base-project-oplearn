package org.oplearn.project.exception.base.topic;

import org.oplearn.project.exception.base.ConflictException;

public class TopicAlreadyFollowedException extends ConflictException {
    public TopicAlreadyFollowedException() {
        super(TopicAlreadyFollowedException.class.getName());
    }
}
