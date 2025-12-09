package org.oplearn.project.exception.base.article;

import org.oplearn.project.exception.base.ConflictException;

public class LinkAlreadyExistedException extends ConflictException {
    public LinkAlreadyExistedException() {
        super("org.oplearn.project.exception.base.article.LinkAlreadyExistedException");
    }
}



