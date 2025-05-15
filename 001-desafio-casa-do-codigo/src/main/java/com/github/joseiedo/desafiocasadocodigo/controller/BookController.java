package com.github.joseiedo.desafiocasadocodigo.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    @PersistenceContext
    private EntityManager entityManager;

//    @PostMapping
//    void create(CreateBookRequest request)
}
