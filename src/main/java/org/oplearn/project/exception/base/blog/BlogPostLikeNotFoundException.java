package org.oplearn.project.exception.base.blog;

import org.oplearn.project.exception.base.NotFoundException;

public class BlogPostLikeNotFoundException extends NotFoundException {
    public BlogPostLikeNotFoundException() {
        super(BlogPostLikeNotFoundException.class.getName());
    }
}
