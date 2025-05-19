package com.github.joseiedo.desafiocasadocodigo.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void setup() {
        try (
                EntityManager entityManager = entityManagerFactory.createEntityManager();
        ) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Country").executeUpdate();
            entityManager.getTransaction().commit();
        }
    }

    @Test
    void shouldCreateSuccessfully() throws Exception {
        String json = """
                {
                    "name": "Brazil"
                }
                """;

        mockMvc.perform(post("/countries")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Brazil")));
    }

    @Test
    void shouldNotCreateWithEmptyName() throws Exception {
        String json = """
                {
                    "name": ""
                }
                """;

        mockMvc.perform(post("/countries")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void shouldNotCreateWithDuplicateName() throws Exception {
        String json = """
                {
                    "name": "Brazil"
                }
                """;

        mockMvc.perform(post("/countries")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Brazil")));

        mockMvc.perform(post("/countries")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("Country already registered"));
    }

    @Test
    void shouldNotCreateWithNullName() throws Exception {
        String json = """
                {
                    "name": null
                }
                """;

        mockMvc.perform(post("/countries")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("must not be blank"));
    }

}