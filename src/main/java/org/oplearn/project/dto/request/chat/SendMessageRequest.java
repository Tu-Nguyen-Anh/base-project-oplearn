package org.oplearn.project.dto.request.chat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SendMessageRequest {
    @NotBlank(message = "Content must not be blank")
    private String content;

    @Pattern(regexp = "TEXT|IMAGE|EMOJI", message = "message_type must be TEXT, IMAGE or EMOJI")
    private String messageType = "TEXT";
}
