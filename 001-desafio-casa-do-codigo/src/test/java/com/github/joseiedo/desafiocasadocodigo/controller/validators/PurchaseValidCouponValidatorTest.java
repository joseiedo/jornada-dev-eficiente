package com.github.joseiedo.desafiocasadocodigo.controller.validators;

import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import com.github.joseiedo.desafiocasadocodigo.repository.purchase.PurchaseRepository;
import com.github.joseiedo.desafiocasadocodigo.shared.ReflectionUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseValidCouponValidatorTest {

    EntityManager entityManager = Mockito.mock(EntityManager.class);
    PurchaseRepository purchaseRepository = Mockito.mock(PurchaseRepository.class);
    PurchaseValidCouponValidator validator = new PurchaseValidCouponValidator(entityManager, purchaseRepository);

    @Test
    void shouldReturnErrorWhenCouponNotFound() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);

        Mockito.when(request.couponId()).thenReturn(1L);
        Mockito.when(entityManager.find(Coupon.class, 1L)).thenReturn(null);

        var errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("couponId"));
        assertEquals("coupon.not.found", errors.getFieldError("couponId").getCode());
        assertEquals("Coupon does not exist", errors.getFieldError("couponId").getDefaultMessage());
    }

    @Test
    void shouldReturnErrorWhenCouponIsExpired() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);
        Coupon coupon = new Coupon("EXPIRED", new BigDecimal(10), LocalDate.now());
        ReflectionUtils.setFieldValue(coupon, "expirationDate", LocalDate.now().minusDays(1));

        Mockito.when(request.couponId()).thenReturn(1L);
        Mockito.when(entityManager.find(Coupon.class, 1L)).thenReturn(coupon);

        var errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("couponId"));
        assertEquals("coupon.expired", errors.getFieldError("couponId").getCode());
        assertEquals("Coupon is expired", errors.getFieldError("couponId").getDefaultMessage());
    }

    @Test
    void shouldReturnErrorWhenCouponIsBeingUsed() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);
        Coupon coupon = new Coupon("USED", new BigDecimal(10), LocalDate.now());

        Mockito.when(request.couponId()).thenReturn(1L);
        Mockito.when(entityManager.find(Coupon.class, 1L)).thenReturn(coupon);
        Mockito.when(purchaseRepository.existsByCouponId(1L)).thenReturn(true);

        var errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("couponId"));
        assertEquals("coupon.already.used", errors.getFieldError("couponId").getCode());
        assertEquals("Coupon has already been used for another purchase", errors.getFieldError("couponId").getDefaultMessage());
    }

    @Test
    void shouldNotReturnErrorWhenCouponIsValid() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);
        Coupon coupon = new Coupon("VALID", new BigDecimal(10), LocalDate.now());

        Mockito.when(request.couponId()).thenReturn(1L);
        Mockito.when(entityManager.find(Coupon.class, 1L)).thenReturn(coupon);
        Mockito.when(purchaseRepository.existsByCouponId(1L)).thenReturn(false);

        var errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, errors);

        assertFalse(errors.hasFieldErrors("couponId"));

    }

}