package com.github.joseiedo.desafiocasadocodigo.dto.book;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;

public record ListBookResponse(
        Long id,
        String title
) {

    public static ListBookResponse fromModel(Book book) {
        return new ListBookResponse(
                book.getId(),
                book.getTitle()
        );
    }
}
