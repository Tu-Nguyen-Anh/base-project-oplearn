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

import static org.oplearn.project.constanst.OpLearnConstants.Message.INVALID_FULL_NAME;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateFullName.FullNameValidator.class)
public @interface ValidateFullName {
  String message() default INVALID_FULL_NAME;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class FullNameValidator implements ConstraintValidator<ValidateFullName, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (Objects.isNull(value)) return true;

      return value.matches("^[\\p{L} ]+$");
    }
  }
}
