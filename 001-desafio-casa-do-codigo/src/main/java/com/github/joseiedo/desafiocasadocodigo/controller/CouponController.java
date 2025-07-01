package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.dto.coupon.CreateCouponRequest;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import com.github.joseiedo.desafiocasadocodigo.repository.purchase.CouponRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponRepository couponRepository;

    public CouponController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @PostMapping
    @Transactional
    public String createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = request.toModel();
        couponRepository.save(coupon);
        return coupon.toString();
    }
}
