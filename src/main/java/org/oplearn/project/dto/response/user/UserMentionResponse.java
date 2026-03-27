package org.oplearn.project.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMentionResponse {
    private Long id;
    private String username;
    private String fullName;
    private String avatar;

    public UserMentionResponse(Long id, String username, String fullName, String avatar) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.avatar = avatar;
    }
}
