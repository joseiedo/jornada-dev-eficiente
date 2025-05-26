package com.github.joseiedo.desafiocasadocodigo.controller.validators;

import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest.RegisterPurchaseOrderRequest.RegisterPurchaseOrderItemRequest;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.repository.book.BookRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PurchaseTotalAndItemsPriceValidator implements Validator {

    private final BookRepository bookRepository;

    public PurchaseTotalAndItemsPriceValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RegisterPurchaseRequest.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        if (errors.hasErrors()) return;
        RegisterPurchaseRequest request = (RegisterPurchaseRequest) target;
        Assert.notNull(request.order().total(), "Total must not be null");
        Assert.notEmpty(request.order().items(), "Items must not be empty");

        Map<Long, RegisterPurchaseOrderItemRequest> bookIds = request.order().items().stream()
                .collect(Collectors.toMap(RegisterPurchaseOrderItemRequest::bookId, item -> item));

        List<Book> existingBookIds = bookRepository.findAllByIdIn(bookIds.keySet());

        if (existingBookIds.size() != bookIds.size()) {
            errors.rejectValue("order.items", "items", "Some book IDs do not exist");
        }

        BigDecimal expectedTotalPrice = existingBookIds.stream()
                .map(book -> book.getPrice()
                        .multiply(new BigDecimal(
                                bookIds.get(book.getId()).quantity())
                        )
                ).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (request.order().total().compareTo(expectedTotalPrice) != 0) {
            errors.rejectValue("order.total", "total", "Total price does not match the sum of item prices");
        }
    }
}
