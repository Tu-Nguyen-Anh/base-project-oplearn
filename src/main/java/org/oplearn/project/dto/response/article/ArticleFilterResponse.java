package org.oplearn.project.dto.response.article;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Data
@Getter
@Setter
public class ArticleFilterResponse {
    private Long id;
    private String title;
    private String link;
    private String guid;
    private String description;
    private Long pubDate;
    private String imageLink;
    private Long topicId;
    private String topicName;
    private String createdBy;
    private Long createdAt;
    private String sourceName;

    public ArticleFilterResponse(Long id, String title, String link, String guid, String description,
                                 Long pubDate, String imageLink, Long topicId, String topicName,
                                 String createdBy, Long createdAt,  String sourceName) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.pubDate = pubDate;
        this.imageLink = imageLink;
        this.topicId = topicId;
        this.topicName = topicName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.sourceName = sourceName;
    }

    public ArticleFilterResponse(Long id, String title, String link, String guid, String description,
                                 Long pubDate, String imageLink, Long topicId, String topicName,
                                 String createdBy, Long createdAt) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.pubDate = pubDate;
        this.imageLink = imageLink;
        this.topicId = topicId;
        this.topicName = topicName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}



