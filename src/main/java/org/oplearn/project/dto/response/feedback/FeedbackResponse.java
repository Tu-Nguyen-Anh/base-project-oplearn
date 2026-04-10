package org.oplearn.project.dto.response.feedback;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackResponse {
    private Long id;
    private Long userId;
    private String userFullName;
    private String userAvatar;
    private String title;
    private String content;
    /** 0=PENDING, 1=IN_REVIEW, 2=RESOLVED, 3=REJECTED */
    private Integer status;
    private String statusLabel;
    private Long createdAt;
    private Long updatedAt;
    private List<FeedbackImageResponse> images;
}
