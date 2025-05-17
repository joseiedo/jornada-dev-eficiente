package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.dto.book.CreateBookRequest;
import com.github.joseiedo.desafiocasadocodigo.dto.book.GetBookResponse;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.book.BookRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.category.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public BookController(AuthorRepository authorRepository, CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public String create(@RequestBody @Valid CreateBookRequest request) {
        Book book = request.toModel(authorRepository, categoryRepository);
        bookRepository.save(book);
        return book.toString();
    }

    @GetMapping
    public Page<GetBookResponse> list(Pageable pageable) {
        return bookRepository.findAll(pageable).map(GetBookResponse::fromModel);
    }
}
