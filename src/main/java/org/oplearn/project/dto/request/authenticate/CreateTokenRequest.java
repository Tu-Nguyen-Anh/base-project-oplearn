package org.oplearn.project.dto.request.authenticate;


import org.oplearn.project.entity.user.enums.TokenType;

import java.util.Map;

public record CreateTokenRequest (
        Long subject,
        TokenType tokenType,
        long expiredSeconds,
        Map<String, String> data
) {
}
