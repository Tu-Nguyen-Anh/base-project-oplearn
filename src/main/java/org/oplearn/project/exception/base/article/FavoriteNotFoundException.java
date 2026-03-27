package org.oplearn.project.exception.base.article;

import org.oplearn.project.exception.base.NotFoundException;

public class FavoriteNotFoundException extends NotFoundException {
    public FavoriteNotFoundException() {
        super(FavoriteNotFoundException.class.getName());
    }
}
