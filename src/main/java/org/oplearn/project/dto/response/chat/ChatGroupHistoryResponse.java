package org.oplearn.project.dto.response.chat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatGroupHistoryResponse {
    private Long id;
    private Long groupId;
    private String action;
    private String message;
    private Long createdAt;
    private String createdBy;
    private String actorFullName;
}
