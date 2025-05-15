package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.dto.author.CreateAuthorRequest;
import com.github.joseiedo.desafiocasadocodigo.dto.author.CreateAuthorResponse;
import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @PostMapping
    @Transactional
    public CreateAuthorResponse create(@RequestBody @Valid CreateAuthorRequest request) {
        Author author = request.toModel();
        authorRepository.save(author);
        return CreateAuthorResponse.fromModel(author);
    }
}