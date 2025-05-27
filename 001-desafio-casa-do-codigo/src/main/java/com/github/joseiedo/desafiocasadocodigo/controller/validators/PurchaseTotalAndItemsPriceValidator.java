package com.github.joseiedo.desafiocasadocodigo.controller.validators;

import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.repository.book.BookRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
        Assert.notNull(request.purchaseOrder().total(), "Total must not be null");
        Assert.notEmpty(request.purchaseOrder().items(), "Items must not be empty");

        if (!request.purchaseOrder().isTotalValid(bookRepository)) {
            errors.rejectValue("purchaseOrder.total", "total", "Total price does not match the sum of item prices");
        }
    }
}
