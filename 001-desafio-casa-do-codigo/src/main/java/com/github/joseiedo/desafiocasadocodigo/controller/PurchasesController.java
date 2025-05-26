package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseCountryAndStateValidator;
import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseTotalAndItemsPriceValidator;
import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.Purchase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    private final PurchaseCountryAndStateValidator purchaseStateValidator;
    private final PurchaseTotalAndItemsPriceValidator purchaseTotalPriceValidator;

    @PersistenceContext
    private EntityManager entityManager;

    public PurchasesController(PurchaseCountryAndStateValidator purchaseStateValidator, PurchaseTotalAndItemsPriceValidator purchaseTotalPriceValidator) {
        this.purchaseStateValidator = purchaseStateValidator;
        this.purchaseTotalPriceValidator = purchaseTotalPriceValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(purchaseStateValidator, purchaseTotalPriceValidator);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @PostMapping
    public String registerPurchase(@Valid @RequestBody RegisterPurchaseRequest request) {
        Purchase purchase = request.toModel(entityManager);
        entityManager.persist(purchase);
        return purchase.toString();
    }
}
