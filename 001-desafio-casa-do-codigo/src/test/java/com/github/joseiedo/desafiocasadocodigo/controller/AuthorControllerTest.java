package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.api.constraints.UniqueChars;
import net.jqwik.api.constraints.UniqueElements;
import net.jqwik.spring.JqwikSpringSupport;
import net.jqwik.web.api.Email;
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
@JqwikSpringSupport
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

    @Property(tries = 10)
    void shouldCreateAuthorWhenValid(
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String name,
            @ForAll @AlphaChars @Email @UniqueElements @UniqueChars String email,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String description
    ) throws Exception {
        String jsonPayload = """
                {
                    "name": "%s",
                    "email": "%s",
                    "description": "%s"
                }
                """.formatted(name, email, description);

        this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());
    }
}