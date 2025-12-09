package org.oplearn.project.dto.response.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse {
    private Long id;
    private String title;
    private String link;
    private String guid;
    private String description;
    private Long pubDate;
    private String imageLink;
    private Long topicId;
    private String topicName;

    public ArticleResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public ArticleResponse(Long id, String title, String link, String guid, String description,
                           Long pubDate, String imageLink, Long topicId, String topicName) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.pubDate = pubDate;
        this.imageLink = imageLink;
        this.topicId = topicId;
        this.topicName = topicName;
    }

    public ArticleResponse(Long id, String title, String link, String guid, String description,
                           Long pubDate, String imageLink) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.pubDate = pubDate;
        this.imageLink = imageLink;
    }
}



