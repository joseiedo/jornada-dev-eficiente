package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrderItem;

import java.math.BigDecimal;

public record GetPurchaseOrderItemResponse(
        String bookTitle,
        String bookAuthor,
        String bookIsbn,
        BigDecimal bookPrice,
        Integer quantity
) {
    static GetPurchaseOrderItemResponse from(PurchaseOrderItem item) {
        return new GetPurchaseOrderItemResponse(
                item.getBook().getTitle(),
                item.getBook().getAuthor().getName(),
                item.getBook().getLsbn(),
                item.getBook().getPrice(),
                item.getQuantity()
        );
    }
}
