package com.github.joseiedo.desafiocasadocodigo.config;

import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import jakarta.validation.ConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueAuthorEmailValidator implements ConstraintValidator<UniqueAuthorEmail, String> {

    @Autowired
    private AuthorRepository authorRepository;

    public UniqueAuthorEmailValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        return !authorRepository.existsByEmail(value);
    }

}
