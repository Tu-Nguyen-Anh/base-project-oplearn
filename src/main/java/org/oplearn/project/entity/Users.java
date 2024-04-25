package org.oplearn.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oplearn.project.entity.base.AuditEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users extends AuditEntity {
  private String username;
  private String password;
  private String name;
  private String email;
  private Addresses address;
}
