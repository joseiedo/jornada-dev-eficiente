package com.github.joseiedo.desafiocasadocodigo.config;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueIgnoreCaseValidator implements ConstraintValidator<UniqueIgnoreCase, String> {

    private Class<?> entity;

    private String column;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueIgnoreCase constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.entity = constraintAnnotation.entity();
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + entity.getSimpleName() + " WHERE " + "lower(" + column + ") = lower(:value)")
                .setParameter("value", value)
                .getSingleResult()).intValue() == 0;
    }

}
