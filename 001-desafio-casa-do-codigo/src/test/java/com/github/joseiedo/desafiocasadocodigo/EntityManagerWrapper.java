package com.github.joseiedo.desafiocasadocodigo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class EntityManagerWrapper {

    private final EntityManagerFactory entityManagerFactory;

    public EntityManagerWrapper(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void runInTransaction(Consumer<EntityManager> consumer) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            consumer.accept(entityManager);
            entityManager.getTransaction().commit();
        }
    }
}
