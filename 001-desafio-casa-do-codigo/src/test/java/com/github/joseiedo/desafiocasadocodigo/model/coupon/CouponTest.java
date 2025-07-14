package com.github.joseiedo.desafiocasadocodigo.model.coupon;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrder;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrderItem;
import com.github.joseiedo.desafiocasadocodigo.shared.ReflectionUtils;
import com.github.joseiedo.desafiocasadocodigo.shared.fakers.BookFaker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    void shouldReturnTrueWhenCouponIsExpired(){
        Coupon coupon = new Coupon("TESTCOUPON", new BigDecimal("12"), LocalDate.now());

        ReflectionUtils.setFieldValue(coupon, "expirationDate", LocalDate.now().minusDays(1));
        boolean isExpired = coupon.isExpired();

        assertTrue(isExpired, "Coupon should be expired");
    }

    @Test
    void shouldReturnFalseWhenCouponIsNotExpired(){
        Coupon coupon = new Coupon("TESTCOUPON", new BigDecimal("12"), LocalDate.now());

        boolean isExpired = coupon.isExpired();

        assertFalse(isExpired, "Coupon should not be expired");
    }


    @ParameterizedTest
    @CsvSource({
            "100.00, 10.0, false, 90.00",
            "200.00, 20.0, false, 160.00",
            "150.00, 15.0, true, 150.00", // Expired coupon should not apply discount
            "50.00, 5.0, false, 47.50",
            "75.00, 25.0, false, 56.25"
    })
    void shouldApplyDiscountCorrectly(BigDecimal total, BigDecimal discountPercentage, Boolean isExpired, BigDecimal expectedTotal) {
        Coupon coupon = new Coupon("TESTCOUPON", discountPercentage, LocalDate.now());
        if (isExpired) {
            ReflectionUtils.setFieldValue(coupon, "expirationDate", LocalDate.now().minusDays(1));
        }
        Book book = BookFaker.validBook().price(total).build();
        PurchaseOrder purchaseOrder = new PurchaseOrder(total, List.of(new PurchaseOrderItem(book, 1)));

        BigDecimal discountedTotal = coupon.applyDiscount(purchaseOrder);

        assertEquals(expectedTotal, discountedTotal, "Discounted total should match expected value");
    }

}