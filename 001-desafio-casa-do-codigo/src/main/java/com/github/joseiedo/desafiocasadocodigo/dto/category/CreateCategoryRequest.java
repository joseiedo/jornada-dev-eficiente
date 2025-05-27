package com.github.joseiedo.desafiocasadocodigo.dto.category;

import com.github.joseiedo.desafiocasadocodigo.config.validators.Unique;
import com.github.joseiedo.desafiocasadocodigo.model.category.Category;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @NotBlank
        @Unique(entity = Category.class, column = "name", message = "Category name already registered")
        String name
) {
    public Category toModel() {
        return new Category(name);
    }
}
