package org.oplearn.project.dto.request.chat;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateGroupRequest {
    @NotBlank(message = "Group name must not be blank")
    private String name;

    private String avatar;

    @NotEmpty(message = "Member list must not be empty")
    private List<Long> memberIds;
}
