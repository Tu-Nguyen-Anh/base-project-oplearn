package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
@Setter
public class TopicRequest {
    @NotBlank(message = "cannot be empty.")
    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String name;

    @NotBlank(message = "cannot be empty.")
    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String url;

    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String rssUrl;

    @Length(max = 5000, message = "must be at most 5000 characters long.")
    private String description;

    @NotNull(message = "cannot be null.")
    private Long sourceId;

    public TopicRequest(
            String name,
            String url,
            String rssUrl,
            String description,
            Long sourceId
    ) {
        this.name = name;
        this.url = url;
        this.rssUrl = rssUrl;
        this.description = description;
        this.sourceId = sourceId;
    }
}



