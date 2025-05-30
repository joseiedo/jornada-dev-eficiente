package com.github.joseiedo.desafiocasadocodigo.fakers;

import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookFaker {
    public static Book.BookBuilder validBook() {
        Author author = new Author("John", "valid_email@gmail.com", "desc");
        return new Book.BookBuilder()
                .title("Effective Java")
                .overview("A comprehensive guide to programming in Java")
                .summary("A summary of Effective Java")
                .price(new BigDecimal("49.99"))
                .numberOfPages(416)
                .lsbn("978-0134686097")
                .publishDate(LocalDate.now().plusDays(10))
                .author(author);
    }
}