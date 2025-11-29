package org.oplearn.project.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

import static org.oplearn.project.constanst.OpLearnConstants.Message.INVALID_EMAIL;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidation.EmailValidator.class)
public @interface EmailValidation {

  String message() default INVALID_EMAIL;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class EmailValidator implements ConstraintValidator<EmailValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (Objects.nonNull(value)) value = value.trim();
      if (Objects.isNull(value) || value.isEmpty()) return true;
//      return value.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]{3,32}@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]{2,8}$");
      return value.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]{3,32}@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$");
    }
  }
}

