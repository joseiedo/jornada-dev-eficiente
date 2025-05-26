package com.github.joseiedo.desafiocasadocodigo.model.book;

import com.github.joseiedo.desafiocasadocodigo.config.Unique;
import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import com.github.joseiedo.desafiocasadocodigo.model.category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String title;
    @NotBlank
    @Length(max = 500)
    private String overview;
    private String summary;
    @NotNull
    @Min(20)
    private BigDecimal price;
    @NotNull
    @Min(100)
    private Integer numberOfPages;
    @NotBlank
    @Column(unique = true)
    private String lsbn;
    @Future
    private LocalDate publishDate;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Deprecated
    public Book() {
    }

    public Book(BookBuilder bookBuilder) {
        this.title = bookBuilder.title;
        this.overview = bookBuilder.overview;
        this.summary = bookBuilder.summary;
        this.price = bookBuilder.price;
        this.numberOfPages = bookBuilder.numberOfPages;
        this.lsbn = bookBuilder.lsbn;
        this.publishDate = bookBuilder.publishDate;
        this.author = bookBuilder.author;
        this.category = bookBuilder.category;
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", summary='" + summary + '\'' +
                ", price=" + price +
                ", numberOfPages=" + numberOfPages +
                ", lsbn='" + lsbn + '\'' +
                ", publishDate=" + publishDate +
                ", author=" + author +
                ", category=" + category +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getSummary() {
        return summary;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public String getLsbn() {
        return lsbn;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public Author getAuthor() {
        return author;
    }

    public Category getCategory() {
        return category;
    }

    public static class BookBuilder {
        private String title;
        private String overview;
        private String summary;
        private BigDecimal price;
        private Integer numberOfPages;
        private String lsbn;
        private LocalDate publishDate;
        private Author author;
        private Category category;

        public BookBuilder title(@NotBlank @Unique(entity = Book.class, column = "title") String title) {
            this.title = title;
            return this;
        }

        public BookBuilder overview(@NotBlank @Length(max = 500) String overview) {
            this.overview = overview;
            return this;
        }

        public BookBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public BookBuilder price(@NotNull @Min(20) BigDecimal price) {
            this.price = price;
            return this;
        }

        public BookBuilder numberOfPages(@NotNull @Min(100) Integer numberOfPages) {
            this.numberOfPages = numberOfPages;
            return this;
        }

        public BookBuilder lsbn(@NotBlank @Unique(entity = Book.class, column = "lsbn") String lsbn) {
            this.lsbn = lsbn;
            return this;
        }

        public BookBuilder publishDate(@Future LocalDate publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public BookBuilder author(@NotNull Author author) {
            this.author = author;
            return this;
        }

        public BookBuilder category(@NotNull Category category) {
            this.category = category;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
