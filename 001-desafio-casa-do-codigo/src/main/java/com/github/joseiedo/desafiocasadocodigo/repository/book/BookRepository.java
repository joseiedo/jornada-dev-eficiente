package com.github.joseiedo.desafiocasadocodigo.repository.book;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface BookRepository extends CrudRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);

    List<Book> findAllByIdIn(Set<Long> ids);
}
