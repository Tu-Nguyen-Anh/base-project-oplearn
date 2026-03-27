package org.oplearn.project.dto.response.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    private Long id;
    private Long senderUserId;
    private String senderFullName;
    private String type;
    private String message;
    private Long articleId;
    private Long commentId;
    private Boolean isRead;
    private Long createdAt;

    public NotificationResponse(Long id, Long senderUserId, String senderFullName, String type,
                                String message, Long articleId, Long commentId,
                                Boolean isRead, Long createdAt) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.senderFullName = senderFullName;
        this.type = type;
        this.message = message;
        this.articleId = articleId;
        this.commentId = commentId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
