package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.config.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

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
        @DecimalMin("1.00")
        BigDecimal total,

        @NotNull
        @NotEmpty
        @Valid
        List<RegisterPurchaseRequestItem> items
) {

    public record RegisterPurchaseRequestItem(
            @NotNull
            @ShouldExist(entity = Book.class, column = "id", message = "Book does not exist")
            Long bookId,

            @NotNull
            @Min(1)
            Integer quantity
    ) {
    }
}
