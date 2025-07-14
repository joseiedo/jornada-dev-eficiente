package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.config.validators.CpfOrCnpj;
import com.github.joseiedo.desafiocasadocodigo.config.validators.NoLetters;
import com.github.joseiedo.desafiocasadocodigo.config.validators.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
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

        @ShouldExist(entity = State.class, column = "id", message = "Coupon does not exist")
        Long couponId,

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
        Purchase purchase = Purchase.builder()
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

        if (couponId != null) {
            Coupon coupon = entityManager.find(Coupon.class, couponId);
            Assert.notNull(coupon, "Coupon not found");
            purchase.setCoupon(coupon);
        }
        return purchase;
    }

    public boolean hasState() {
        return stateId != null;
    }
}
