package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.dto.author.CreateAuthorRequest;
import com.github.joseiedo.desafiocasadocodigo.dto.author.CreateAuthorResponse;
import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping
    @Transactional
    public CreateAuthorResponse cadastrar(@RequestBody @Valid CreateAuthorRequest request) {
        Author author = request.toModel();
        entityManager.persist(author);
        return CreateAuthorResponse.fromModel(author);
    }
}