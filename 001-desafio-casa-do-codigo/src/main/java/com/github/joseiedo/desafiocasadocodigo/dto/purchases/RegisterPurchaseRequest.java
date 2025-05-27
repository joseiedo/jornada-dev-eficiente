package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.config.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.Purchase;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrder;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

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
        String postalCode,

        @NotNull
        @Valid
        RegisterPurchaseOrderRequest purchaseOrder
) {

    public Purchase toModel(
            EntityManager entityManager
    ) {
        Country country = entityManager.find(Country.class, countryId);
        Assert.notNull(country, "Country must not be null");
        State state = stateId != null ? entityManager.find(State.class, stateId) : null;

        PurchaseOrder purchaseOrder = this.purchaseOrder().toModel(entityManager);

        return Purchase.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .document(document)
                .address(address)
                .complement(complement)
                .city(city)
                .country(country)
                .state(state)
                .phone(phone)
                .postalCode(postalCode)
                .order(purchaseOrder)
                .build();
    }

    public boolean hasState() {
        return stateId != null;
    }
}
