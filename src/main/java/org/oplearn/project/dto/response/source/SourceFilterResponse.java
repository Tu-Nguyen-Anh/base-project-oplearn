package org.oplearn.project.dto.response.source;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Data
@Getter
@Setter
public class SourceFilterResponse {
    private Long id;
    private String name;
    private String url;
    private String avatar;
    private Integer type;
    private String description;
    private String createdBy;
    private Long createdAt;
    private Boolean active;

    public SourceFilterResponse(Long id, String name, String url, String avatar, Integer type,
                                String description, String createdBy, Long createdAt, Boolean active) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.avatar = avatar;
        this.type = type;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.active = active;
    }
}


