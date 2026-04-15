
package org.oplearn.project.dto.response.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.oplearn.project.entity.user.enums.UserRole;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Data
@Getter
@Setter
public class UserFilterResponse {
  private Long id;

  private String username;

  private String email;

  private String fullName;

  private String phone;

  private int status;

  private UserRole role;

  private String createdBy;

  private Long createdAt;


  public UserFilterResponse(Long id, String username, String email, String fullName, String phone,
                            int status, UserRole role, String createdBy, Long createdAt) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.phone = phone;
    this.status = status;
    this.role = role;
    this.createdBy = createdBy;
    this.createdAt = createdAt;
  }

}
