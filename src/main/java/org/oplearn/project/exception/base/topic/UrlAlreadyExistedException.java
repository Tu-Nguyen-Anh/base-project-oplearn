package org.oplearn.project.exception.base.topic;

import org.oplearn.project.exception.base.ConflictException;

public class UrlAlreadyExistedException extends ConflictException {
    public UrlAlreadyExistedException() {
        super("org.oplearn.project.exception.base.topic.UrlAlreadyExistedException");
    }
}



