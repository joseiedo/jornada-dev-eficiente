package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.dto.coupon.CreateCouponRequest;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping
    @Transactional
    public String createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = request.toModel();
        entityManager.persist(coupon);
        return coupon.toString();
    }
}
