package com.github.joseiedo.desafiocasadocodigo.model.purchase;

import com.github.joseiedo.desafiocasadocodigo.fakers.BookFaker;
import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

class PurchaseOrderTest {

    @ParameterizedTest
    @CsvSource({
            "100.00, true",
            "99.99, false",
            "100.01, false",
    })
    public void checkIfTotalMatchesOrderTotal(BigDecimal value, boolean expected) {
        Author author = new Author("John Doe", "johndoe@gmail.com", "An author description");
        Category category = new Category("Programming");
        Book book = BookFaker.validBook().author(author).price(value).category(category).build();

        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(book, 1);

        if (!expected) {
            IllegalArgumentException purchaseOrder = Assertions.assertThrows(IllegalArgumentException.class, () -> {
                new PurchaseOrder(new BigDecimal("100.00"), List.of(purchaseOrderItem));
            });
            Assertions.assertEquals("Total is not valid for the items provided.", purchaseOrder.getMessage());
        } else {
            PurchaseOrder purchaseOrder = new PurchaseOrder(new BigDecimal("100.00"), List.of(purchaseOrderItem));
            Assertions.assertTrue(purchaseOrder.totalIsValid());
        }
    }
}