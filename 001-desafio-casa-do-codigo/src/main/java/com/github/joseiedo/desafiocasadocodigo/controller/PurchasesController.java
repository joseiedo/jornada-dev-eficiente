package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseCountryAndStateValidator;
import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseTotalAndItemsPriceValidator;
import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    private final PurchaseCountryAndStateValidator purchaseStateValidator;
    private final PurchaseTotalAndItemsPriceValidator purchaseTotalPriceValidator;

    public PurchasesController(PurchaseCountryAndStateValidator purchaseStateValidator, PurchaseTotalAndItemsPriceValidator purchaseTotalPriceValidator) {
        this.purchaseStateValidator = purchaseStateValidator;
        this.purchaseTotalPriceValidator = purchaseTotalPriceValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(purchaseStateValidator, purchaseTotalPriceValidator);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String registerPurchase(@Valid @RequestBody RegisterPurchaseRequest request) {
        return "Purchase registered successfully";
    }
}
