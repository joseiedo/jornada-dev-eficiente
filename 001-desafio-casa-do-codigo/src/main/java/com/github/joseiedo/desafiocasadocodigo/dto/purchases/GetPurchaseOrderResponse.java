package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrder;

import java.math.BigDecimal;

public record GetPurchaseOrderResponse(
        Long id,
        BigDecimal total,
        GetPurchaseOrderItemResponse[] items
) {
    static GetPurchaseOrderResponse from(PurchaseOrder purchaseOrder) {
        GetPurchaseOrderItemResponse[] items = purchaseOrder.getItems().stream()
                .map(GetPurchaseOrderItemResponse::from)
                .toArray(GetPurchaseOrderItemResponse[]::new);
        return new GetPurchaseOrderResponse(
                purchaseOrder.getId(),
                purchaseOrder.getTotal(),
                items
        );
    }
}
