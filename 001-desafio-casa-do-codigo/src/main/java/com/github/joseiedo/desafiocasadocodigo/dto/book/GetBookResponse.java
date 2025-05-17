package com.github.joseiedo.desafiocasadocodigo.dto.book;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;

public record GetBookResponse(
        Long id,
        String title
) {

    public static GetBookResponse fromModel(Book book) {
        return new GetBookResponse(
                book.getId(),
                book.getTitle()
        );
    }
}
