package com.github.joseiedo.desafiocasadocodigo.fakers;

import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CouponFaker {

    public static Coupon.CouponBuilder validCoupon() {
        return Coupon.builder()
                .code("VALIDCOUPON")
                .discountPercentage(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(30));
    }

    public static Coupon.CouponBuilder expiredCoupon() {
        return Coupon.builder()
                .code("EXPIREDCOUPON")
                .discountPercentage(new BigDecimal("5.0"))
                .expirationDate(LocalDate.now().minusDays(1));
    }
}
