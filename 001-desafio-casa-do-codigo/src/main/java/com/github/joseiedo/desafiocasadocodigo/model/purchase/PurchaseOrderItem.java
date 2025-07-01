package com.github.joseiedo.desafiocasadocodigo.model.purchase;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Embeddable
public class PurchaseOrderItem {
    @NotNull
    @ManyToOne
    private Book book;
    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal priceAtMoment;

    @Deprecated
    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(@NotNull Book book, @NotNull @Min(1) Integer quantity) {
        this.book = book;
        this.priceAtMoment = book.getPrice();
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return priceAtMoment.multiply(new BigDecimal(quantity));
    }

    public Book getBook() {
        return book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceAtMoment() {
        return priceAtMoment;
    }
}
