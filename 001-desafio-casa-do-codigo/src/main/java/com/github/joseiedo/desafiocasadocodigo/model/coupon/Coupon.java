package com.github.joseiedo.desafiocasadocodigo.model.coupon;

import com.github.joseiedo.desafiocasadocodigo.config.validators.Unique;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String code;

    @Positive
    @NotNull
    private BigDecimal discountPercentage;

    @NotNull
    @Future
    private LocalDate expirationDate;

    @Deprecated
    public Coupon() {
    }

    public Coupon(
            @NotNull @Unique(entity = Coupon.class, column = "code") String code,
            @Positive @NotNull BigDecimal discountPercentage,
            @NotNull @Future LocalDate expirationDate
    ) {
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expirationDate = expirationDate;
    }

    private Coupon(CouponBuilder builder) {
        this.code = builder.code;
        this.discountPercentage = builder.discountPercentage;
        this.expirationDate = builder.expirationDate;
    }

    public static CouponBuilder builder() {
        return new CouponBuilder();
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", expirationDate=" + expirationDate +
                '}';
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public static class CouponBuilder {
        private String code;
        private BigDecimal discountPercentage;
        private LocalDate expirationDate;

        public CouponBuilder code(@NotBlank @Unique(entity = Coupon.class, column = "code") String code) {
            this.code = code;
            return this;
        }

        public CouponBuilder discountPercentage(@NonNull @Positive BigDecimal discountPercentage) {
            this.discountPercentage = discountPercentage;
            return this;
        }

        public CouponBuilder expirationDate(@NonNull @Future LocalDate expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Coupon build() {
            return new Coupon(this);
        }
    }
}
