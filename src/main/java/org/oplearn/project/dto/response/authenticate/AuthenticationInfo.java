package org.oplearn.project.dto.response.authenticate;

import lombok.Data;

@Data
public class AuthenticationInfo {

  private Long userId;

  private String userName;

  public AuthenticationInfo(Long id, String username) {
    this.userId = id;
    this.userName = username;
  }
}
