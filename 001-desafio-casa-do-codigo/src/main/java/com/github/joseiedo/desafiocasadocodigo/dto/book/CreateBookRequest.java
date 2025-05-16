package com.github.joseiedo.desafiocasadocodigo.dto.book;

import com.github.joseiedo.desafiocasadocodigo.config.ShouldExist;
import com.github.joseiedo.desafiocasadocodigo.config.Unique;
import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.category.Category;
import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.category.CategoryRepository;
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
        @Length(min = 1, max = 500)
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

        @NotNull
        @ShouldExist(entity = Category.class, column = "id", message = "Category does not exist")
        Long categoryId,

        @NotNull
        @ShouldExist(entity = Author.class, column = "id", message = "Author does not exist")
        Long authorId
) {
    public Book toModel(AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        Author author = authorRepository.findById(authorId).orElseThrow(IllegalArgumentException::new);
        Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
        return Book.builder()
                .title(title)
                .overview(overview)
                .summary(summary)
                .price(price)
                .numberOfPages(numberOfPages)
                .lsbn(lsbn)
                .publishDate(publishDate)
                .category(category)
                .author(author)
                .build();
    }
}
