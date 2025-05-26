package com.github.joseiedo.desafiocasadocodigo.controller.validators;

import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest.RegisterPurchaseRequestItem;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.repository.book.BookRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
        Assert.notNull(request.total(), "Total must not be null");
        Assert.notEmpty(request.items(), "Items must not be empty");

        Set<Long> bookIds = request.items().stream()
                .map(RegisterPurchaseRequestItem::bookId)
                .collect(Collectors.toSet());

        List<Book> existingBookIds = bookRepository.findAllByIdIn(bookIds);

        if (existingBookIds.size() != bookIds.size()) {
            errors.rejectValue("items", "items", "Some book IDs do not exist");
        }

        BigDecimal expectedTotalPrice = existingBookIds.stream()
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (request.total().compareTo(expectedTotalPrice) != 0) {
            errors.rejectValue("total", "total", "Total price does not match the sum of item prices");
        }
    }
}
