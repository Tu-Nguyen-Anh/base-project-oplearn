package org.oplearn.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("users")
public class User {
  @Id
  @JsonFormat
  private String id;
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
  @Field("is_deleted")
  private Boolean isDeleted;
  public User(String username, String password, String name, String email, List<String> addressId) {
    this.id = String.valueOf(UUID.randomUUID());
    this.username = username;
    this.password = password;
    this.name = name;
    this.email = email;
    this.addressId = addressId;
    this.isDeleted= false;
  }

}
