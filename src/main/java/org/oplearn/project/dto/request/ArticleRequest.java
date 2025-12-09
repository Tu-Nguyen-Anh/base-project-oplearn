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
public class ArticleRequest {
    @NotBlank(message = "cannot be empty.")
    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String title;

    @NotBlank(message = "cannot be empty.")
    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String link;

    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String guid;

    @Length(max = 5000, message = "must be at most 5000 characters long.")
    private String description;

    @Length(max = 255, message = "must be at most 255 characters long.")
    private Long pubDate;

    @Length(max = 5000, message = "must be at most 5000 characters long.")
    private String imageLink;

    @NotNull(message = "cannot be null.")
    private Long topicId;

    public ArticleRequest(
            String title,
            String link,
            String guid,
            String description,
            Long pubDate,
            String imageLink,
            Long topicId
    ) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.pubDate = pubDate;
        this.imageLink = imageLink;
        this.topicId = topicId;
    }
}



