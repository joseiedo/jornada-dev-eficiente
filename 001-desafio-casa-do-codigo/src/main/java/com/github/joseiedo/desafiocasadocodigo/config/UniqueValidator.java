package com.github.joseiedo.desafiocasadocodigo.config;

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
        return value != null && entityManager.createNativeQuery("SELECT * FROM " + entity.getSimpleName() + " WHERE " + column + " = :value")
                .setParameter("value", value)
                .getResultList()
                .isEmpty();
    }

}
