package org.oplearn.project.dto.request.chat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SendMessageRequest {
    @NotBlank(message = "Content must not be blank")
    private String content;

    private String messageType = "TEXT";
}
