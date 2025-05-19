package com.github.joseiedo.desafiocasadocodigo.dto.book;

import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.category.Category;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DetailedBookResponse(
        Long id,
        String title,
        String overview,
        String summary,
        BigDecimal price,
        Integer numberOfPages,
        String lsbn,
        LocalDate publishDate,
        DetailedBookAuthorResponse author,
        DetailedBookCategoryResponse category
) {

    public static DetailedBookResponse fromModel(@NotNull Book book) {
        return new DetailedBookResponse(
                book.getId(),
                book.getTitle(),
                book.getOverview(),
                book.getSummary(),
                book.getPrice(),
                book.getNumberOfPages(),
                book.getLsbn(),
                book.getPublishDate(),
                DetailedBookAuthorResponse.fromModel(book.getAuthor()),
                DetailedBookCategoryResponse.fromModel(book.getCategory())
        );
    }

}

record DetailedBookAuthorResponse(
        Long id,
        String name,
        String email,
        String description
) {
    public static DetailedBookAuthorResponse fromModel(Author author) {
        return new DetailedBookAuthorResponse(
                author.getId(),
                author.getName(),
                author.getEmail(),
                author.getDescription()
        );
    }
}

record DetailedBookCategoryResponse(
        Long id,
        String name
) {
    public static DetailedBookCategoryResponse fromModel(Category category) {
        return new DetailedBookCategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
