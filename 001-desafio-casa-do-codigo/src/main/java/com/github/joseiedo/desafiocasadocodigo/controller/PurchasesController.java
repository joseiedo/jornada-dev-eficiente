package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.controller.validators.PurchaseStateValidator;
import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    public PurchaseStateValidator purchaseStateValidator;

    public PurchasesController(PurchaseStateValidator purchaseStateValidator) {
        this.purchaseStateValidator = purchaseStateValidator;
    }

    @PostMapping
    public ResponseEntity<String> registerPurchase(@Valid @RequestBody RegisterPurchaseRequest request) {
        Errors errors = purchaseStateValidator.validateObject(request);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body("A valid state must be provided for the given country");
        }
        return ResponseEntity.ok("Purchase registered successfully");
    }
}
