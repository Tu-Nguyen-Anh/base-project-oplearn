package org.oplearn.project.dto.request.authenticate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewPasswordRequest {
  @Size(max = 32, message = "Mật khẩu không được vượt quá 100 ký tự")
  private String newPassword;

  @Size(max = 32, message = "Mật khẩu không được vượt quá 100 ký tự")
  private String confirmPassword;
}
