package org.oplearn.project.dto.response.blog;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BlogPostFilterResponse {
    private Long id;
    private Long userId;
    private String authorName;
    private String authorAvatar;
    private String title;
    private String content;
    private String imageUrl;
    private Integer visibility;
    private Long createdAt;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Boolean liked;

    // Constructor for JPQL
    public BlogPostFilterResponse(Long id, Long userId, String authorName, String authorAvatar,
                                   String title, String content, String imageUrl,
                                   Integer visibility, Long createdAt) {
        this.id = id;
        this.userId = userId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.likeCount = 0L;
        this.commentCount = 0L;
        this.shareCount = 0L;
        this.liked = false;
    }
}
