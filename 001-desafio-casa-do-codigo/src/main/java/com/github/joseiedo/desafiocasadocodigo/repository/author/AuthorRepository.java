package com.github.joseiedo.desafiocasadocodigo.repository.author;

import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
