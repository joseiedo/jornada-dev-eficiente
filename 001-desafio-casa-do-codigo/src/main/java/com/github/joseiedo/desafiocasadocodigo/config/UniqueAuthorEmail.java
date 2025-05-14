package com.github.joseiedo.desafiocasadocodigo.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = UniqueAuthorEmailValidator.class)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface UniqueAuthorEmail {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "Email already registered";

    Class<?> entity();
}
