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

import static org.oplearn.project.constanst.OpLearnConstants.Message.INVALID_PHONE_NUMBER;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidation.PhoneNumberValidator.class)
public @interface PhoneNumberValidation {
  String message() default INVALID_PHONE_NUMBER;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (Objects.isNull(value) || value.isEmpty()) return true;
      return value.matches("(84|0[35789])(\\d{8})\\b");
    }
  }

}

