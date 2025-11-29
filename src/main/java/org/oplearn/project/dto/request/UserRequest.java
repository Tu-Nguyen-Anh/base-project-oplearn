package org.oplearn.project.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.oplearn.project.annotation.PhoneNumberValidation;
import org.oplearn.project.annotation.ValidationName;

import static org.oplearn.project.constanst.OpLearnConstants.ActiveStatus.ACTIVE;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    @ValidationName
    @Length(max = 50, message = "must be between 3 and 50 characters long.")
    private String username;

    @Length(max = 50, message = "must be 50 characters long.")
    private String fullName;

    @NotBlank(message = "cannot be empty.")
    @Length(max = 256, message = "must be at least 256 characters long.")
    private String email;

    @PhoneNumberValidation
    private String phoneNumber;

    private int status;

    public UserRequest(
            String username,
            String fullName,
            String email,
            String phoneNumber
    ) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = ACTIVE;
    }
}