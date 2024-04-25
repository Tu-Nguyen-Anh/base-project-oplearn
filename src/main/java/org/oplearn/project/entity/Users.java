package org.oplearn.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("users")
public class Users  {
  @Id
  @JsonFormat
  private Integer id;
  @Field("username")
  private String username;
  @Field("password")
  private String password;
  @Field("name")
  private String name;
  @Field("email")
  private String email;
  @Field("address_id")
  private List<String> addressId;
}
