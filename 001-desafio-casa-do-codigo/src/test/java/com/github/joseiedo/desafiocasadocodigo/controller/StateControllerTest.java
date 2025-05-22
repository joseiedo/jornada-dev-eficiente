package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.EntityManagerWrapper;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import org.junit.jupiter.api.AfterEach;
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
class StateControllerTest {

    Country country;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerWrapper entityManagerWrapper;

    @BeforeEach
    void setup() {
        entityManagerWrapper.runInTransaction(em -> {
            country = new Country("Brazil");
            em.persist(country);
        });
    }

    @AfterEach
    void tearDown() {
        entityManagerWrapper.runInTransaction(em -> {
            em.createQuery("DELETE FROM State s WHERE s.country = :country")
                    .setParameter("country", country)
                    .executeUpdate();
            em.remove(em.contains(country) ? country : em.merge(country));
        });
    }


    @Test
    void shouldCreateSuccessfully() throws Exception {
        String json = """
                {
                    "name": "São Paulo",
                    "countryId": "%d"
                }
                """.formatted(country.getId());

        mockMvc.perform(post("/states")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("São Paulo")));
    }

    @Test
    void shouldNotCreateWithInvalidCountry() throws Exception {
        String json = """
                {
                    "name": "São Paulo",
                    "countryId": "%d"
                }
                """.formatted(999999);

        mockMvc.perform(post("/states")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.countryId").value("Country not found"));
    }

    @Test
    void shouldNotCreateWithDuplicateName() throws Exception {
        String json = """
                {
                    "name": "São Paulo",
                    "countryId": "%d"
                }
                """.formatted(country.getId());

        mockMvc.perform(post("/states")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(containsString("São Paulo")));

        mockMvc.perform(post("/states")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("State already registered"));
    }

    @Test
    void shouldNotCreateWithEmptyName() throws Exception {
        String json = """
                {
                    "name": "",
                    "countryId": "%d"
                }
                """.formatted(country.getId());

        mockMvc.perform(post("/states")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("must not be blank"));
    }

    @Test
    void shouldNotCreateWithNullName() throws Exception {
        String json = """
                {
                    "name": null,
                    "countryId": "%d"
                }
                """.formatted(country.getId());

        mockMvc.perform(post("/states")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.name").value("must not be blank"));
    }
}