package com.github.joseiedo.desafiocasadocodigo.model.author;

import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
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
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Length(max = 400)
    private String description;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    private java.util.Set<Book> books = new java.util.HashSet<>();

    @Deprecated
    public Author() {
    }

    public Author(@NotBlank String name, @Email @NotBlank String email, @NotBlank String description) {
        this.name = name;
        this.email = email;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
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
