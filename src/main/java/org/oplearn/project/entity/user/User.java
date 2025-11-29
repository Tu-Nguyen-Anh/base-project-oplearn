package org.oplearn.project.entity.user;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.AuditEntity;

import static org.oplearn.project.constanst.OpLearnConstants.ActiveStatus.ACTIVE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_PASSWORD;
import static org.oplearn.project.utils.PasswordEncoderUtils.getPasswordEncoder;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
@Data
public class User extends AuditEntity {
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phoneNumber;
    @Column(name = "avatar")
    private String avatar;
    private Integer status;
    private Boolean deleted;

    public User(
            String username,
            String fullName,
            String email,
            String phoneNumber,
            Integer status
    ) {
        this.username = username;
        this.fullName = fullName;
        this.password = getPasswordEncoder().encode(DEFAULT_PASSWORD);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.deleted = false;
    }

    public User(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public User(String username) {
        this.username = username;
    }

    public User(
            String username,
            String fullName,
            String email,
            String phoneNumber
    ) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = getPasswordEncoder().encode(DEFAULT_PASSWORD);
        this.phoneNumber = phoneNumber;
        this.status = ACTIVE;
        this.deleted = false;
    }
}
