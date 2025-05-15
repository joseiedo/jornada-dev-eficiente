package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.dto.category.CreateCategoryRequest;
import com.github.joseiedo.desafiocasadocodigo.dto.category.CreateCategoryResponse;
import com.github.joseiedo.desafiocasadocodigo.model.category.Category;
import com.github.joseiedo.desafiocasadocodigo.repository.category.CategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    @Transactional
    public CreateCategoryResponse cadastrar(@RequestBody @Valid CreateCategoryRequest request) {
        Category category = request.toModel();
        categoryRepository.save(category);
        return CreateCategoryResponse.fromModel(category);
    }
}