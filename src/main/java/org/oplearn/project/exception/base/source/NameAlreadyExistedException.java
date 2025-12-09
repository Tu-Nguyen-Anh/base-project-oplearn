package org.oplearn.project.exception.base.source;

import org.oplearn.project.exception.base.ConflictException;

public class NameAlreadyExistedException extends ConflictException {
    public NameAlreadyExistedException() {
        super("org.oplearn.project.exception.base.source.NameAlreadyExistedException");
    }
}



