package org.oplearn.project.dto.response.article;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FavoriteArticleResponse {
    private Long id;
    private Long userId;
    private Long articleId;
    private String title;
    private String link;
    private String imageLink;
    private Long pubDate;
    private String topicName;
    private String sourceName;
    private Long createdAt;

    public FavoriteArticleResponse(Long id, Long userId, Long articleId, String title, String link,
                                   String imageLink, Long pubDate, String topicName, String sourceName,
                                   Long createdAt) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.title = title;
        this.link = link;
        this.imageLink = imageLink;
        this.pubDate = pubDate;
        this.topicName = topicName;
        this.sourceName = sourceName;
        this.createdAt = createdAt;
    }
}
