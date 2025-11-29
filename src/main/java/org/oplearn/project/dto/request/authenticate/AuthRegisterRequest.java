package org.oplearn.project.dto.request.authenticate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.oplearn.project.annotation.*;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRegisterRequest {
  @NotBlank(message = "Username cannot be blank")
  @NotEmpty(message = "Username cannot be empty")
  @Size(max = 50, message = "Username must not exceed 50 characters")
  @ValidationName
  private String username;
  @ValidationPassword
  @Size(max = 32, message = "Mật khẩu không được vượt quá 100 ký tự")
  private String password;
  @ValidateFullName
  private String fullName;
  @EmailValidation
  private String email;
  @PhoneNumberValidation
  private String phoneNumber;
}
