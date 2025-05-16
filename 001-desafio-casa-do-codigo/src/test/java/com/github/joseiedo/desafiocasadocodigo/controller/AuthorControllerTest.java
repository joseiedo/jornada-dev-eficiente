package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Test
    void shouldReturnBadRequestWhenInvalidEmail() throws Exception {
        String jsonPayload = """
                {
                    "name": "John Doe",
                    "email": "invalid_email.com",
                    "description": "Author of several books"
                }
                """;

        this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("must be a well-formed email address"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidName() throws Exception {
        String jsonPayload = """
                {
                    "name": " ",
                    "email": "johndoe@gmail.com",
                    "description": "Author of several books"
                }
                """;

        this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidDescription() throws Exception {
        String jsonPayload = """
                {
                    "name": "John Doe",
                    "email": "johndoe@gmail.com",
                    "description": " "
                }
                """;

        this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.description").value("must not be blank"));
    }

    @Test
    void shouldNotAllowDuplicatedEmail() throws Exception {
        String jsonPayload = """
                {
                    "name": "John Doe",
                    "email": "johndoe@example.com",
                    "description": "Author of several books"
                }
                """;

        this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());

        this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Email already registered"));
    }

    @Test
    void shouldCreateAuthorWhenValid() throws Exception {
        String jsonPayload = """
                {
                    "name": "John Doe",
                    "email": "johndoe@example.com",
                    "description": "Author of several books"
                }
                """;

        this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());
    }
}