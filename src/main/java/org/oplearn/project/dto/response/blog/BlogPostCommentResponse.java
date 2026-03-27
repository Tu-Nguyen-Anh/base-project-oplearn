package org.oplearn.project.dto.response.blog;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlogPostCommentResponse {
    private Long id;
    private Long postId;
    private Long userId;
    private String authorName;
    private String authorAvatar;
    private Long parentCommentId;
    private String content;
    private Long createdAt;

    // Constructor for JPQL
    public BlogPostCommentResponse(Long id, Long postId, Long userId, String authorName,
                                    String authorAvatar, Long parentCommentId,
                                    String content, Long createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
