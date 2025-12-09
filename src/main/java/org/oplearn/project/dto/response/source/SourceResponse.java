package org.oplearn.project.dto.response.source;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceResponse {
    private Long id;
    private String name;
    private String url;
    private String avatar;
    private Integer type;
    private String description;

    public SourceResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SourceResponse(Long id, String name, String url, String avatar, Integer type, String description) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.avatar = avatar;
        this.type = type;
        this.description = description;
    }
}



