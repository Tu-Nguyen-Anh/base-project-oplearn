package org.oplearn.project.dto.response.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageResponse {
    private Long id;
    private Long groupId;
    private Long senderId;
    private String senderUsername;
    private String senderFullName;
    private String senderAvatar;
    private String content;
    private String messageType;
    private Long createdAt;
    private List<ReaderResponse> readers;
    private List<ReactionResponse> reactions;
}
