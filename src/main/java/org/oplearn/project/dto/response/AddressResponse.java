package org.oplearn.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AddressResponse {
  private Integer id;
  private String province;
  private String district;
  private String ward;
}
