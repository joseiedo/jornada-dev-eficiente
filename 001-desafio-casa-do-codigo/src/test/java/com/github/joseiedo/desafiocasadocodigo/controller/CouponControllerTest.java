package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.EntityManagerWrapper;
import com.github.joseiedo.desafiocasadocodigo.fakers.CouponFaker;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerWrapper entityManagerWrapper;

    @AfterEach
    void tearDown() {
        entityManagerWrapper.runInTransaction(em -> {
            em.createNativeQuery("DELETE FROM Coupon").executeUpdate();
        });
    }

    @Test
    void shouldReturnBadRequestWhenNonNullFieldsAreNull() throws Exception {
        String payload = """
                {}
                """;


        mockMvc.perform(
                        post("/coupons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.code").value("must not be null"))
                .andExpect(jsonPath("$.errors.discountPercentage").value("must not be null"))
                .andExpect(jsonPath("$.errors.expirationDate").value("must not be null"));
    }

    @Test
    void shouldReturnBadRequestWhenExpirationDateIsFromPast() throws Exception {
        String payload = """
                {
                    "expirationDate": "2003-09-25"
                }
                """;


        mockMvc.perform(
                        post("/coupons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.expirationDate").value("must be a future date"));
    }

    @Test
    void shouldReturnBadRequestWhenCodeIsNotUnique() throws Exception {
        Coupon existingCoupon = CouponFaker.validCoupon().build();
        String payload = """
                {
                    "code": "%s",
                    "discountPercentage": %s,
                    "expirationDate": "%s"
                }
                """.formatted(existingCoupon.getCode(), existingCoupon.getDiscountPercentage(), existingCoupon.getExpirationDate());


        entityManagerWrapper.runInTransaction(em -> em.persist(existingCoupon));

        mockMvc.perform(
                        post("/coupons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.code").value("Coupon code already exists"))
        ;
    }

    @Test
    void shouldReturnOkWhenCreatingSuccessfully() throws Exception {
        String payload = """
                {
                    "code": "UNO",
                    "discountPercentage": 0.01,
                    "expirationDate": "%s"
                }
                """.formatted(LocalDate.now().plusDays(1).toString());


        mockMvc.perform(
                post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)).andExpect(status().isOk());
    }
}