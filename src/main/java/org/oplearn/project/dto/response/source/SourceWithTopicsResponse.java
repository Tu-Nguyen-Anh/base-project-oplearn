package org.oplearn.project.dto.response.source;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.dto.response.topic.TopicSimpleResponse;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceWithTopicsResponse {
    private Long id;
    private String name;
    private String url;
    private String avatar;
    private Integer type;
    private String description;
    private List<TopicSimpleResponse> topics;
}



