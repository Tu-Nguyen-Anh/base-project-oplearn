package org.oplearn.project.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddReactionRequest {
    @NotBlank(message = "Emoji must not be blank")
    private String emoji;
}
