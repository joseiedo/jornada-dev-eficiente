package com.github.joseiedo.desafiocasadocodigo.dto.author;

import com.github.joseiedo.desafiocasadocodigo.config.Unique;
import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateAuthorRequest(
        @NotBlank
        String name,

        @NotNull
        @Email
        @Unique(entity = Author.class, column = "email", message = "Email already registered")
        String email,

        @NotBlank
        @Length(max = 400)
        String description
) {

    public Author toModel() {
        return new Author(name, email, description);
    }
}
