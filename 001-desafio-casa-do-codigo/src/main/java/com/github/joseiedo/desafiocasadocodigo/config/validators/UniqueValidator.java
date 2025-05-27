package com.github.joseiedo.desafiocasadocodigo.config.validators;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private Class<?> entity;

    private String column;

    @Autowired
    private EntityManager entityManager;

    @Override
    public void initialize(Unique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.entity = constraintAnnotation.entity();
        this.column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) return true;
        return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM " + entity.getSimpleName() + " WHERE " + column + " = :value")
                .setParameter("value", value)
                .getSingleResult()).intValue() == 0;
    }

}
