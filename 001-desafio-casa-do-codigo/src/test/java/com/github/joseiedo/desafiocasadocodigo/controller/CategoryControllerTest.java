package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.repository.category.CategoryRepository;
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
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnBadRequestWhenInvalidName() throws Exception {
        String jsonPayload = """
                {
                    "name": " "
                }
                """;

        MvcResult mvcResult = this.mockMvc
                .perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"errors\":{\"name\":\"must not be blank\"}}", responseBody);
    }

    @Test
    void shouldNotAllowDuplicatedName() throws Exception {
        String jsonPayload = """
                {
                    "name": "Adventure"
                }
                """;

        this.mockMvc
                .perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(System.out::println)
                .andExpect(status().isOk()).andReturn();

        MvcResult mvcResult = this.mockMvc
                .perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("{\"errors\":{\"name\":\"Category name already registered\"}}", responseBody);
    }

    @Test
    void shouldCreateCategoryWhenValid() throws Exception {
        String jsonPayload = """
                {
                    "name": "Comedy"
                }
                """;

        this.mockMvc
                .perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk()).andReturn();
    }

}