package com.github.joseiedo.desafiocasadocodigo.model.purchase;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class PurchaseOrderItem {
    @NotNull
    @ManyToOne
    private Book book;
    @NotNull
    @Min(1)
    private Integer quantity;

    @Deprecated
    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(@NotNull Book book, @NotNull @Min(1) Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }
}
