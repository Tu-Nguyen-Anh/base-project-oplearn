package org.oplearn.project.dto.response.authenticate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRegisterResponse {
  private Integer id;
  private String username;
  private String fullName;
  private String email;
  private String phoneNumber;
  private int isActive;
}
