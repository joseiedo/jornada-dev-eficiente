package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.config.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterPurchaseRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @CpfOrCnpj
        @NotBlank
        String document,

        @NotBlank
        String address,

        @NotBlank
        String complement,

        @NotBlank
        String city,

        @NotNull
        @ShouldExist(entity = Country.class, column = "id", message = "Country does not exist")
        Long countryId,

        @ShouldExist(entity = State.class, column = "id", message = "State does not exist")
        Long stateId,

        @NotBlank
        @NoLetters
        String phone,

        @NotNull
        @NoLetters
        String postalCode
) {
}
