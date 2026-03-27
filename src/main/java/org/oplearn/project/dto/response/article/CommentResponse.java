package org.oplearn.project.dto.response.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private Long id;
    private Long articleId;
    private Long userId;
    private String username;
    private String fullName;
    private String avatar;
    private String content;
    private Long createdAt;
    private List<MentionedUserResponse> mentionedUsers;

    public CommentResponse(Long id, Long articleId, Long userId, String username,
                           String fullName, String avatar, String content, Long createdAt) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.avatar = avatar;
        this.content = content;
        this.createdAt = createdAt;
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MentionedUserResponse {
        private Long userId;
        private String username;
        private String fullName;

        public MentionedUserResponse(Long userId, String username, String fullName) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
        }
    }
}
