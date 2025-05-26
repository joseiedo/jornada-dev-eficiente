package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.config.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.Purchase;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrder;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrderItem;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(
                this.purchaseOrder.total(),
                this.purchaseOrder.items().stream()
                        .map(item -> {
                            Book book = entityManager.find(Book.class, item.bookId());
                            return new PurchaseOrderItem(book, item.quantity());
                        }).toList()
        );

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

    public record RegisterPurchaseOrderRequest(
            @NotNull
            @DecimalMin("1.00")
            BigDecimal total,

            @NotNull
            @NotEmpty
            @Valid
            List<RegisterPurchaseOrderItemRequest> items
    ) {

        public record RegisterPurchaseOrderItemRequest(
                @NotNull
                @ShouldExist(entity = Book.class, column = "id", message = "Book does not exist")
                Long bookId,

                @NotNull
                @Min(1)
                Integer quantity
        ) {
        }
    }

}
