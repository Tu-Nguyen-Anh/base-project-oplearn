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
public class Addresses extends AuditEntity {
  private String province;
  private String district;
  private String ward;
  private Boolean isDeleted;
}
