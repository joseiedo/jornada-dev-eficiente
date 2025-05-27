package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.config.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrderItem;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public record RegisterPurchaseOrderItemRequest(
        @NotNull
        @ShouldExist(entity = Book.class, column = "id", message = "Book does not exist")
        Long bookId,

        @NotNull
        @Min(1)
        Integer quantity
) {

    public PurchaseOrderItem toModel(EntityManager entityManager) {
        Book book = entityManager.find(Book.class, bookId);
        Assert.notNull(book, "Book must not be null");
        return new PurchaseOrderItem(book, quantity);
    }

    public BigDecimal getTotalPrice(Book book) {
        Assert.notNull(book, "Book must not be null");
        return book.getPrice().multiply(new BigDecimal(quantity));
    }
}
