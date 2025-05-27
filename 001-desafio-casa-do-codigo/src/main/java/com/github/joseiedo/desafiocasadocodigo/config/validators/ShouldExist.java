package com.github.joseiedo.desafiocasadocodigo.config.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = ShouldExistValidator.class)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ShouldExist {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> entity();

    String column();

    String message() default "{com.github.joseiedo.desafiocasadocodigo.config.ExistsById.message}";
}
