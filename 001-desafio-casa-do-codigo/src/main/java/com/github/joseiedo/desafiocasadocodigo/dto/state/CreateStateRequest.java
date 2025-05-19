package com.github.joseiedo.desafiocasadocodigo.dto.state;

import com.github.joseiedo.desafiocasadocodigo.config.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.config.UniqueIgnoreCase;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateStateRequest(

        @NotBlank
        @UniqueIgnoreCase(
                entity = State.class,
                column = "name",
                message = "State already registered"
        )
        String name,

        @ShouldExist(
                entity = Country.class,
                column = "id",
                message = "Country not found"
        )
        @NotNull
        Long countryId
) {
    public State toModel(EntityManager entityManager) {
        Country country = entityManager.find(Country.class, countryId);
        if (country == null) {
            throw new IllegalStateException("Country not found");
        }

        return new State(name, country);
    }
}
