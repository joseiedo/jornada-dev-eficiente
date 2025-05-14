package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        MvcResult mvcResult = this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"errors\":{\"email\":\"must be a well-formed email address\"}}", responseBody);
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

        MvcResult mvcResult = this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"errors\":{\"name\":\"must not be blank\"}}", responseBody);
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

        MvcResult mvcResult = this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"errors\":{\"description\":\"must not be blank\"}}", responseBody);
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
                .andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = this.mockMvc
                .perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"errors\":{\"email\":\"Email already registered\"}}", responseBody);
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
                .andExpect(status().isOk()).andReturn();
    }

}