package com.github.joseiedo.desafiocasadocodigo.dto.coupon;

import com.github.joseiedo.desafiocasadocodigo.config.validators.Unique;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCouponRequest(
        @NotNull
        @Unique(entity = Coupon.class, column = "code", message = "Coupon code already exists")
        String code,

        @Positive
        @NotNull
        BigDecimal discountPercentage,

        @NotNull
        @Future
        LocalDate expirationDate
) {

    public Coupon toModel() {
        return new Coupon(code, discountPercentage, expirationDate);
    }
}
