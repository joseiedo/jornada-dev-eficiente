package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.dto.country.CreateCountryRequest;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping
    @Transactional
    String create(@Valid @RequestBody CreateCountryRequest request) {
        Country country = request.toModel();
        entityManager.persist(country);
        return request.toString();
    }
}
