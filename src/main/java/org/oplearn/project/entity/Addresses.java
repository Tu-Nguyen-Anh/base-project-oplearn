package org.oplearn.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.FieldResult;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("addresses")
public class Addresses {
  @Id
  @JsonFormat
  private Integer id;
  @Field("province")
  private String province;
  @Field("district")
  private String district;
  @Field("ward")
  private String ward;
  @Field("is_deleted")
  private boolean isDeleted;

  public Addresses(Integer id, String province, String district, String ward) {
    this.id = id;
    this.province = province;
    this.district = district;
    this.ward = ward;
    this.isDeleted = false;
  }
}
