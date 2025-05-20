package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import jakarta.validation.Constraint;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.PARAMETER})
@Pattern(regexp = "[^A-Za-z]+")
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@Constraint(validatedBy = {})
public @interface NoLetters {

    String message() default "must not contain letters";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
