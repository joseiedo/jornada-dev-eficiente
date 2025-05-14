package com.github.joseiedo.desafiocasadocodigo.model.author;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String name;

    @NotNull
    @Email
    @Column(unique = true)
    String email;

    @NotBlank
    @Length(max = 400)
    String description;

    @CreationTimestamp
    ZonedDateTime createdAt;

    // Created just because JPA requires it, It shouldn't be used.
    @Deprecated
    public Author() {
    }

    public Author(@NotBlank String name, @Email @NotBlank String email, @NotBlank String description) {
        this.name = name;
        this.email = email;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
