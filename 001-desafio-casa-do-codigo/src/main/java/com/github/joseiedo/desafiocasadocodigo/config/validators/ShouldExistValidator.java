package com.github.joseiedo.desafiocasadocodigo.config.validators;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ShouldExistValidator implements ConstraintValidator<ShouldExist, Object> {

    @PersistenceContext
    private EntityManager entityManager;
    private Class<?> entity;
    private Object column;

    @Override
    public void initialize(ShouldExist annotation) {
        ConstraintValidator.super.initialize(annotation);
        column = annotation.column();
        entity = annotation.entity();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String query = "SELECT COUNT(*) FROM " + entity.getSimpleName() + " WHERE " + column + " = :value";
        if (value == null) return true;
        return ((Number) entityManager.createNativeQuery(query)
                .setParameter("value", value)
                .getSingleResult()).intValue() > 0;
    }
}
