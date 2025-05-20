package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PurchasesControllerTest {

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
            entityManager.createQuery("DELETE FROM State").executeUpdate();
            entityManager.createQuery("DELETE FROM Country").executeUpdate();
            entityManager.getTransaction().commit();
        }
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String payload = """
                {
                    "email": "invalid-email",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "123.456.789-00",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": 1,
                    "stateId": 1,
                    "phone": "123456789",
                    "postalCode": "12345"
                }
                """;

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("must be a well-formed email address"));
    }

    @Test
    void shouldReturnBadRequestWhenFirstNameIsBlank() throws Exception {
        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": " ",
                    "lastName": "Doe",
                    "document": "123.456.789-00",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": 1,
                    "stateId": 1,
                    "phone": "123456789",
                    "postalCode": "12345"
                }
                """;

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").value("must not be blank"));
    }

    @Test
    void shouldReturnBadRequestWhenDocumentIsInvalid() throws Exception {
        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "invalid-document",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": 1,
                    "stateId": 1,
                    "phone": "123456789",
                    "postalCode": "12345"
                }
                """;

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.document").value("Document must be a valid CPF or CNPJ"));
    }

    @Test
    void shouldReturnBadRequestWhenPhoneContainsLetters() throws Exception {
        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "123.456.789-00",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": 1,
                    "stateId": 1,
                    "phone": "123ABC789",
                    "postalCode": "12345"
                }
                """;

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phone").value("must not contain letters"));
    }

    @Test
    void shouldReturnBadRequestWhenCountryDoesNotExist() throws Exception {
        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "123.456.789-00",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": 999,
                    "stateId": 1,
                    "phone": "123456789",
                    "postalCode": "12345"
                }
                """;

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.countryId").value("Country does not exist"));
    }


    @Test
    void shouldReturnBadRequestWhenStateIsNullAndCountryHasStates() throws Exception {
        Country countryWithStates = new Country("BRAZIL");
        State state = new State("São Paulo", countryWithStates);
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(countryWithStates);
            entityManager.persist(state);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }

        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "142.809.830-54",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": %d,
                    "stateId": null,
                    "phone": "123456789",
                    "postalCode": "12345"
                }
                """.formatted(countryWithStates.getId());

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("A valid state must be provided for the given country"));
    }

    @Test
    void shouldReturnBadRequestWhenPostalCodeContainsLetters() throws Exception {
        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "123.456.789-00",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": 1,
                    "stateId": 1,
                    "phone": "123456789",
                    "postalCode": "123AB"
                }
                """;

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.postalCode").value("must not contain letters"));
    }

    @Test
    void shouldReturn200WhenNoErrors() throws Exception {
        Country countryWithStates = new Country("BRAZIL");
        State state = new State("São Paulo", countryWithStates);
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(countryWithStates);
            entityManager.persist(state);
            entityManager.flush();
            entityManager.getTransaction().commit();
        }

        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "142.809.830-54",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "countryId": %d,
                    "stateId": %d,
                    "phone": "123456789",
                    "postalCode": "12345"
                }
                """.formatted(countryWithStates.getId(), state.getId());

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }
}
