package org.oplearn.project.exception.base.blog;

import org.oplearn.project.exception.base.NotFoundException;

public class BlogPostNotFoundException extends NotFoundException {
    public BlogPostNotFoundException() {
        super(BlogPostNotFoundException.class.getName());
    }
}
