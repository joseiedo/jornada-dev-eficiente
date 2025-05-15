package com.github.joseiedo.desafiocasadocodigo.dto.category;

import com.github.joseiedo.desafiocasadocodigo.model.category.Category;

public record CreateCategoryResponse(
        Long id,
        String name
) {

    public static CreateCategoryResponse fromModel(Category category) {
        return new CreateCategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}