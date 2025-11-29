package org.oplearn.project.dto.request.authenticate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import org.oplearn.project.annotation.ValidationPassword;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChangePasswordRequest(
      String oldPassword,
      @ValidationPassword
      @Size(max = 32, message = "Mật khẩu không được vượt quá 100 ký tự")
      String newPassword,
      String confirmPassword
) {
}


