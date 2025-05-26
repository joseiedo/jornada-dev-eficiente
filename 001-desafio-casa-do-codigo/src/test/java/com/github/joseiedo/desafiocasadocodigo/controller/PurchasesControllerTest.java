package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.EntityManagerWrapper;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchasesControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private EntityManagerWrapper entityManagerWrapper;

    @BeforeEach
    void setup() {
        entityManagerWrapper.runInTransaction(em -> {
            em.createQuery("DELETE FROM State").executeUpdate();
            em.createQuery("DELETE FROM Book").executeUpdate();
            em.createQuery("DELETE FROM Author").executeUpdate();
            em.createQuery("DELETE FROM Country").executeUpdate();
        });
        try (EntityManager entityManager = entityManagerFactory.createEntityManager();) {
            entityManager.getTransaction().begin();
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

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.countryId").value("Country does not exist"));
    }

    @Test
    void shouldReturnBadRequestWhenStateIsNullAndCountryHasStates() throws Exception {
        Country countryWithStates = new Country("BRAZIL");
        State state = new State("São Paulo", countryWithStates);
        Book book = BookFactory.validBook().price(new BigDecimal("20.00")).build();

        entityManagerWrapper.runInTransaction(em -> {
            em.persist(countryWithStates);
            em.persist(state);
            em.persist(book);
        });

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
                    "postalCode": "12345",
                    "total": 20.0,
                    "items": [
                        {
                            "bookId": %d,
                            "quantity": 1
                        }
                    ]
                }
                """.formatted(countryWithStates.getId(), book.getId());

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.stateId").value("Country has states, state ID must be provided"));
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

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.postalCode").value("must not contain letters"));
    }

    @Test
    void shouldReturnBadRequestWhenTotalDoesntMatchExpected() throws Exception {
        Country countryWithStates = new Country("BRAZIL");
        State state = new State("São Paulo", countryWithStates);
        Book book = BookFactory.validBook().price(new BigDecimal("100.00")).build();

        entityManagerWrapper.runInTransaction(em -> {
            em.persist(countryWithStates);
            em.persist(state);
            em.persist(book);
        });

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
                    "postalCode": "12345",
                    "total": 20.0,
                    "items": [
                        {
                            "bookId": %d,
                            "quantity": 1
                        }
                    ]
                }
                """.formatted(countryWithStates.getId(), state.getId(), book.getId());

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.total").value("Total price does not match the sum of item prices"));
        ;

    }

    @Test
    void shouldReturnBadRequestIfBooksDontExist() throws Exception {
        Country countryWithStates = new Country("BRAZIL");
        State state = new State("São Paulo", countryWithStates);

        entityManagerWrapper.runInTransaction(em -> {
            em.persist(countryWithStates);
            em.persist(state);
        });

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
                    "postalCode": "12345",
                    "total": 20.0,
                    "items": [
                        {
                            "bookId": %d,
                            "quantity": 1
                        }
                    ]
                }
                """.formatted(countryWithStates.getId(), state.getId(), -1);

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['items[0].bookId']").value("Book does not exist"));
    }

    @Test
    void shouldReturnCreatedWhenNoErrors() throws Exception {
        Country countryWithStates = new Country("BRAZIL");
        State state = new State("São Paulo", countryWithStates);
        Book book = BookFactory.validBook().price(new BigDecimal("20.00")).build();

        entityManagerWrapper.runInTransaction(em -> {
            em.persist(countryWithStates);
            em.persist(state);
            em.persist(book);
        });

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
                    "postalCode": "12345",
                    "total": 20.0,
                    "items": [
                        {
                            "bookId": %d,
                            "quantity": 1
                        }
                    ]
                }
                """.formatted(countryWithStates.getId(), state.getId(), book.getId());

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isCreated());

    }
}
