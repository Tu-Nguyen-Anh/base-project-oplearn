package org.oplearn.project.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
  private Long id;
  private String username;
  private String fullName;
  private String avatar;
  private String phoneNumber;
  private String email;
  private int status;

  public UserResponse(Long id, String username) {
    this.id = id;
    this.username = username;
  }

  public UserResponse(Long id, String username, String fullName, String phoneNumber, String email, String avatar) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.avatar = avatar;
  }

  public UserResponse(Long id, String fullName, String username) {
    this.id = id;
    this.fullName = fullName;
    this.username = username;
  }

  public UserResponse(Long id, String username, String fullName, String phoneNumber, String email, int status) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.status = status;
  }

  public UserResponse(Long id, String fullName, String username, Integer status) {
    this.id = id;
    this.fullName = fullName;
    this.username = username;
    this.status = status;
  }
}
