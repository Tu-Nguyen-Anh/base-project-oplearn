package org.oplearn.project.dto.response.topic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicResponse {
    private Long id;
    private String name;
    private String url;
    private String rssUrl;
    private String description;
    private Long sourceId;
    private String sourceName;
    private Boolean active;

    public TopicResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TopicResponse(Long id, String name, String url, String rssUrl, String description, Boolean active) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.rssUrl = rssUrl;
        this.description = description;
        this.active = active;
    }
}



