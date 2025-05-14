package com.github.joseiedo.desafiocasadocodigo.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private Class<?> entity;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        return value != null && entityManager.createNativeQuery("SELECT * FROM " + entity.getSimpleName() + " WHERE email = :email")
                .setParameter("email", value)
                .getResultList()
                .isEmpty();
    }

}
