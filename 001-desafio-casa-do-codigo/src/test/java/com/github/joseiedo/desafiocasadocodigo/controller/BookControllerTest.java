package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.model.author.Author;
import com.github.joseiedo.desafiocasadocodigo.model.category.Category;
import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.book.BookRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.category.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    Author author;
    Category category;
    LocalDate publishDate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        author = new Author("Test", "email@goo.com", "Broadway");
        authorRepository.save(author);
        category = new Category("Test Category");
        categoryRepository.save(category);
        publishDate = LocalDate.now().plusDays(1);
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsNotUnique() throws Exception {
        String jsonPayload = """
                {
                    "title": "Unique Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "%s",
                    "categoryId": %d,
                    "authorId": %d
                }
                """.formatted(publishDate.toString(), category.getId(), author.getId());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.title").value("Book title already exists"));
    }

    @Test
    void shouldReturnBadRequestWhenOverviewExceedsMaxLength() throws Exception {

        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "%s",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "2024-12-01",
                    "categoryId": 1,
                    "authorId": 1
                }
                """.formatted("A".repeat(501));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.overview").value("length must be between 1 and 500"));
    }

    @Test
    void shouldReturnBadRequestWhenPriceIsBelowMinimum() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 10,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "2024-12-01",
                    "categoryId": 1,
                    "authorId": 1
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.price").value("must be greater than or equal to 20"));
    }

    @Test
    void shouldReturnBadRequestWhenNumberOfPagesIsBelowMinimum() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 50,
                    "lsbn": "123-456-789",
                    "publishDate": "2024-12-01",
                    "categoryId": 1,
                    "authorId": 1
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.numberOfPages").value("must be greater than or equal to 100"));
    }

    @Test
    void shouldReturnBadRequestWhenLsbnIsNotUnique() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "%s",
                    "categoryId": %d,
                    "authorId": %d
                }
                """.formatted(publishDate.toString(), category.getId(), author.getId());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.lsbn").value("Book lsbn already exists"));
    }

    @Test
    void shouldReturnBadRequestWhenPublishDateIsNotInFuture() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "2022-01-01",
                    "categoryId": 1,
                    "authorId": 1
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.publishDate").value("must be a future date"));
    }

    @Test
    void shouldReturnBadRequestWhenCategoryIdDoesNotExist() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "2024-12-01",
                    "categoryId": -1,
                    "authorId": 1
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.categoryId").value("Category does not exist"));
    }

    @Test
    void shouldReturnBadRequestWhenAuthorIdDoesNotExist() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "2024-12-01",
                    "categoryId": 1,
                    "authorId": 999
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("errors.authorId").value("Author does not exist"));
    }

    @Test
    void shouldReturnOkWhenSucessOnCreation() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "%s",
                    "categoryId": %d,
                    "authorId": %d
                }
                """.formatted(publishDate.toString(), category.getId(), author.getId());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());
    }

    @Test
    void shouldListAllBooks() throws Exception {
        String jsonPayload = """
                {
                    "title": "Valid Title",
                    "overview": "Valid overview",
                    "summary": "Valid summary",
                    "price": 30,
                    "numberOfPages": 150,
                    "lsbn": "123-456-789",
                    "publishDate": "%s",
                    "categoryId": %d,
                    "authorId": %d
                }
                """.formatted(publishDate.toString(), category.getId(), author.getId());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").isArray())
                .andExpect(jsonPath("content[0].id").isNumber())
                .andExpect(jsonPath("content[0].title").value("Valid Title"));
    }
}
