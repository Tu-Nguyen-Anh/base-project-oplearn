package org.oplearn.project.dto.response.topic;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FollowedTopicResponse {
    private Long id;
    private Long topicId;
    private String topicName;
    private String topicUrl;
    private String rssUrl;
    private Long sourceId;
    private String sourceName;
    private Long followedAt;

    public FollowedTopicResponse(Long id, Long topicId, String topicName, String topicUrl,
                                  String rssUrl, Long sourceId, String sourceName, Long followedAt) {
        this.id = id;
        this.topicId = topicId;
        this.topicName = topicName;
        this.topicUrl = topicUrl;
        this.rssUrl = rssUrl;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.followedAt = followedAt;
    }
}
