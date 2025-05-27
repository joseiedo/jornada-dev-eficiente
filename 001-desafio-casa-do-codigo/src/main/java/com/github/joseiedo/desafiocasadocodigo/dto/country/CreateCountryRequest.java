package com.github.joseiedo.desafiocasadocodigo.dto.country;

import com.github.joseiedo.desafiocasadocodigo.config.validators.UniqueIgnoreCase;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import jakarta.validation.constraints.NotBlank;

public record CreateCountryRequest(
        @NotBlank
        @UniqueIgnoreCase(entity = Country.class, column = "name", message = "Country already registered")
        String name
) {
    public Country toModel() {
        return new Country(name);
    }
}
