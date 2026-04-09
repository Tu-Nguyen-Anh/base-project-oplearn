package org.oplearn.project.exception.base.topic;

import org.oplearn.project.exception.base.NotFoundException;

public class FollowTopicNotFoundException extends NotFoundException {
    public FollowTopicNotFoundException() {
        super(FollowTopicNotFoundException.class.getName());
    }
}
