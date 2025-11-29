package org.oplearn.project.dto.request.authenticate;


import org.oplearn.project.exception.base.user.PasswordNotNullException;
import org.oplearn.project.exception.base.user.UsernameNotNullException;

public record LoginRequest(String username, String password) {
    public LoginRequest {
        if (username == null || username.isBlank()) {
            throw new UsernameNotNullException();
        }
        if (password == null || password.isBlank()) {
            throw new PasswordNotNullException();
        }
    }
}