package com.github.joseiedo.desafiocasadocodigo.controller.validators;

import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PurchaseValidCouponValidator implements Validator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RegisterPurchaseRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        RegisterPurchaseRequest request = (RegisterPurchaseRequest) target;
        if (request.couponId() == null) return;

        Coupon coupon = entityManager.find(Coupon.class, request.couponId());

        if (coupon == null) {
            errors.rejectValue("couponId", "coupon.not.found", "Coupon does not exist");
            return;
        } else if (coupon.isExpired()) {
            errors.rejectValue("couponId", "coupon.expired", "Coupon is expired");
            return;
        }

        Boolean couponUsed = entityManager.createQuery("SELECT COUNT(p) > 0 FROM Purchase p WHERE p.coupon.id = :couponId", Boolean.class)
                .setParameter("couponId", request.couponId())
                .getSingleResult();

        if (couponUsed) {
            errors.rejectValue("couponId", "coupon.already.used", "Coupon has already been used for another purchase");
        }
    }
}
