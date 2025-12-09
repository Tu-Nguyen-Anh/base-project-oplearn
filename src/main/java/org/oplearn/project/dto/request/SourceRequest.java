package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Getter
@Setter
public class SourceRequest {
    @NotBlank(message = "cannot be empty.")
    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String name;

    @NotBlank(message = "cannot be empty.")
    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String url;

    @Length(max = 1024, message = "must be at most 1024 characters long.")
    private String avatar;

    private Integer type;

    @Length(max = 5000, message = "must be at most 5000 characters long.")
    private String description;

    public SourceRequest(
            String name,
            String url,
            String avatar,
            Integer type,
            String description
    ) {
        this.name = name;
        this.url = url;
        this.avatar = avatar;
        this.type = type;
        this.description = description;
    }
}



