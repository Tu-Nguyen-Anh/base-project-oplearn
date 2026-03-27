package org.oplearn.project.exception.base.article;

import org.oplearn.project.exception.base.ConflictException;

public class ArticleAlreadyFavoritedException extends ConflictException {
    public ArticleAlreadyFavoritedException() {
        super(ArticleAlreadyFavoritedException.class.getName());
    }
}
