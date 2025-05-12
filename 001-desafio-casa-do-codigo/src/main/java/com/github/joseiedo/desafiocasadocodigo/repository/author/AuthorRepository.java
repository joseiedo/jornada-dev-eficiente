package com.github.joseiedo.desafiocasadocodigo.repository.author;

import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
