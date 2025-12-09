package org.oplearn.project.dto.response.topic;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Data
@Getter
@Setter
public class TopicFilterResponse {
    private Long id;
    private String name;
    private String url;
    private String rssUrl;
    private String description;
    private Long sourceId;
    private String sourceName;
    private String createdBy;
    private Long createdAt;

    public TopicFilterResponse(Long id, String name, String url, String rssUrl, String description,
                               Long sourceId, String sourceName, String createdBy, Long createdAt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.rssUrl = rssUrl;
        this.description = description;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}



