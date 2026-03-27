package org.oplearn.project.dto.response.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserProfileResponse {
    private Long id;
    private String username;
    private String fullName;
    private String avatar;
    private String email;
    private Long totalPosts;
    private PageResponse<BlogPostFilterResponse> posts;
}
