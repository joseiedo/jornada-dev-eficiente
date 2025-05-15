package com.github.joseiedo.desafiocasadocodigo.dto.book;

import com.github.joseiedo.desafiocasadocodigo.config.Unique;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBookRequest(
        @NotBlank
        @Unique(entity = Book.class, column = "title", message = "Book title already exists")
        String title,

        @NotBlank
        @Length(max = 500)
        String overview,

        String summary,

        @NotNull
        @Min(20)
        BigDecimal price,

        @NotNull
        @Min(100)
        Integer numberOfPages,

        @NotBlank
        @Unique(entity = Book.class, column = "lsbn", message = "Book lsbn already exists")
        String lsbn,

        @Future
        LocalDate publishDate,
        Long categoryId,
        Long authorId
) {
}
