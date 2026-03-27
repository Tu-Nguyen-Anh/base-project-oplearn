package org.oplearn.project.exception.base.blog;

import org.oplearn.project.exception.base.ConflictException;

public class BlogPostAlreadyLikedException extends ConflictException {
    public BlogPostAlreadyLikedException() {
        super(BlogPostAlreadyLikedException.class.getName());
    }
}
