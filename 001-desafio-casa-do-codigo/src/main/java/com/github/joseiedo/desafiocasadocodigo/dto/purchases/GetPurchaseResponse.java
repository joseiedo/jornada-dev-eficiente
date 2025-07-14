package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.model.purchase.Purchase;

import java.math.BigDecimal;

public record GetPurchaseResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String document,
        String address,
        String complement,
        String city,
        Long countryId,
        Boolean hasCoupon,
        BigDecimal totalWithDiscount,
        Long stateId,
        String phone,
        String postalCode,
        GetPurchaseOrderResponse purchaseOrder
) {


    public static GetPurchaseResponse from(Purchase purchase) {
        return new GetPurchaseResponse(
                purchase.getId(),
                purchase.getEmail(),
                purchase.getFirstName(),
                purchase.getLastName(),
                purchase.getDocument(),
                purchase.getAddress(),
                purchase.getComplement(),
                purchase.getCity(),
                purchase.getCountry().getId(),
                purchase.hasCoupon(),
                purchase.getTotalWithDiscount(),
                purchase.getState() != null ? purchase.getState().getId() : null,
                purchase.getPhone(),
                purchase.getPostalCode(),
                GetPurchaseOrderResponse.from(purchase.getPurchaseOrder())
        );
    }

}
