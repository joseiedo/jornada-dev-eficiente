package com.github.joseiedo.desafiocasadocodigo.dto.author;

import com.github.joseiedo.desafiocasadocodigo.model.author.Author;

import java.time.ZonedDateTime;

public record CreateAuthorResponse(
        Long id,
        String name,
        String email,
        String description,
        ZonedDateTime createdAt
) {

    public static CreateAuthorResponse fromModel(Author model) {
        return new CreateAuthorResponse(
                model.getId(),
                model.getName(),
                model.getEmail(),
                model.getDescription(),
                model.getCreatedAt()
        );
    }
}
