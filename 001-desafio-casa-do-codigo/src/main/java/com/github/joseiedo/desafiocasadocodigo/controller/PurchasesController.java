package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.config.exception.NotFoundException;
import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseCountryAndStateValidator;
import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseTotalAndItemsPriceValidator;
import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseValidCouponValidator;
import com.github.joseiedo.desafiocasadocodigo.dto.purchases.GetPurchaseResponse;
import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.Purchase;
import com.github.joseiedo.desafiocasadocodigo.repository.purchase.PurchaseRepository;
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
    private final PurchaseValidCouponValidator validCouponValidator;
    private final PurchaseRepository purchaseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public PurchasesController(PurchaseCountryAndStateValidator purchaseStateValidator, PurchaseTotalAndItemsPriceValidator purchaseTotalPriceValidator, PurchaseValidCouponValidator purchaseValidCouponValidator, PurchaseRepository purchaseRepository) {
        this.purchaseStateValidator = purchaseStateValidator;
        this.purchaseTotalPriceValidator = purchaseTotalPriceValidator;
        this.validCouponValidator = purchaseValidCouponValidator;
        this.purchaseRepository = purchaseRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(purchaseStateValidator, purchaseTotalPriceValidator, validCouponValidator);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @PostMapping
    public String registerPurchase(@Valid @RequestBody RegisterPurchaseRequest request) {
        Purchase purchase = request.toModel(entityManager);
        purchaseRepository.save(purchase);
        return purchase.getId().toString();
    }

    @GetMapping("/{id}")
    public GetPurchaseResponse getPurchase(@PathVariable Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id: " + id));

        return GetPurchaseResponse.from(purchase);
    }
}
