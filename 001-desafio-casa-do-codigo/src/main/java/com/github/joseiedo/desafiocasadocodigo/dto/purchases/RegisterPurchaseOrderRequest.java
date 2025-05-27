package com.github.joseiedo.desafiocasadocodigo.dto.purchases;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrder;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrderItem;
import com.github.joseiedo.desafiocasadocodigo.repository.book.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RegisterPurchaseOrderRequest(
        @NotNull
        @DecimalMin("1.00")
        BigDecimal total,

        @NotNull
        @NotEmpty
        @Valid
        List<RegisterPurchaseOrderItemRequest> items
) {

    public PurchaseOrder toModel(EntityManager entityManager) {
        List<PurchaseOrderItem> items = this.items.stream().map(item -> item.toModel(entityManager)).toList();
        return new PurchaseOrder(this.total(), items);
    }

    public Boolean isTotalValid(BookRepository bookRepository) {
        Map<Long, RegisterPurchaseOrderItemRequest> itemsGroupedByBookID = getItemsGroupedByBookID();
        List<Book> books = bookRepository.findAllByIdIn(itemsGroupedByBookID.keySet());
        Assert.isTrue(books.size() == itemsGroupedByBookID.size(), "Received items size differs from found books size");

        BigDecimal sum = books.stream()
                .map(book -> itemsGroupedByBookID.get(book.getId()).getTotalPrice(book))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.compareTo(total) == 0;
    }

    public Map<Long, RegisterPurchaseOrderItemRequest> getItemsGroupedByBookID() {
        return items.stream()
                .collect(Collectors
                        .toMap(RegisterPurchaseOrderItemRequest::bookId, item -> item));
    }
}
