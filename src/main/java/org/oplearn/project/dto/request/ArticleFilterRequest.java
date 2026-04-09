package org.oplearn.project.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class ArticleFilterRequest {
    private Integer page;
    private Integer size;
    private String keyword;
    private Long topicId;
    private Long sourceId;
    private String fromPubDate; // DD/MM/YYYY
    private String toPubDate; // DD/MM/YYYY
    private Boolean followedOnly;

    @JsonIgnore
    private List<Long> followedTopicIds;
}



