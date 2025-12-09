package org.oplearn.project.exception.base.source;

import org.oplearn.project.exception.base.ConflictException;

public class UrlAlreadyExistedException extends ConflictException {
    public UrlAlreadyExistedException() {
        super("org.oplearn.project.exception.base.source.UrlAlreadyExistedException");
    }
}



