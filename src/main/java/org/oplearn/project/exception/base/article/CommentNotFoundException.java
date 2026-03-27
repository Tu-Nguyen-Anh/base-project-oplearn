package org.oplearn.project.exception.base.article;

import org.oplearn.project.exception.base.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException() {
        super(CommentNotFoundException.class.getName());
    }
}
