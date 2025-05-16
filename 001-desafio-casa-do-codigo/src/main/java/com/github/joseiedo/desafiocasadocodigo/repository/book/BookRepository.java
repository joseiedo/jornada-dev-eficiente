package com.github.joseiedo.desafiocasadocodigo.repository.book;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
