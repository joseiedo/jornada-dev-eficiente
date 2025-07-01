package com.github.joseiedo.desafiocasadocodigo.controller;

import com.github.joseiedo.desafiocasadocodigo.EntityManagerWrapper;
import com.github.joseiedo.desafiocasadocodigo.fakers.BookFaker;
import com.github.joseiedo.desafiocasadocodigo.fakers.CouponFaker;
import com.github.joseiedo.desafiocasadocodigo.model.book.Book;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.coupon.Coupon;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.Purchase;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrder;
import com.github.joseiedo.desafiocasadocodigo.model.purchase.PurchaseOrderItem;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import com.github.joseiedo.desafiocasadocodigo.repository.author.AuthorRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.book.BookRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.purchase.CouponRepository;
import com.github.joseiedo.desafiocasadocodigo.repository.purchase.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchasesControllerTest {

    Country countryWithStates;
    State state;
    Book book;
    Coupon validCoupon;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManagerWrapper entityManagerWrapper;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setup() {
        book = BookFaker.validBook().price(new BigDecimal("20.00")).build();
        validCoupon = CouponFaker.validCoupon().build();
        couponRepository.save(validCoupon);
        bookRepository.save(book);

        entityManagerWrapper.runInTransaction(em -> {

            countryWithStates = new Country("BRAZIL");
            state = new State("São Paulo", countryWithStates);
            em.persist(countryWithStates);
            em.persist(state);
        });
    }

    @AfterEach
    void tearDown() {
        purchaseRepository.deleteAll();
        couponRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        entityManagerWrapper.runInTransaction(em -> {
            em.remove(em.find(Country.class, countryWithStates.getId()));
        });
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
                    "purchaseOrder": {
                        "total": 20.0,
                        "items": [
                            {
                                "bookId": %d,
                                "quantity": 1
                            }
                        ]
                    }
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
                    "purchaseOrder": {
                        "total": 20.0,
                        "items": [
                            {
                                "bookId": %d,
                                "quantity": 10
                            }
                        ]
                    }
                }
                """.formatted(countryWithStates.getId(), state.getId(), book.getId());

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.['purchaseOrder.total']").value("Total price does not match the sum of item prices"));
        ;

    }

    @Test
    void shouldReturnBadRequestIfBooksDontExist() throws Exception {
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
                    "purchaseOrder": {
                        "total": 20.0,
                        "items": [
                            {
                                "bookId": %d,
                                "quantity": 1
                            }
                        ]
                    }
                }
                """.formatted(countryWithStates.getId(), state.getId(), -1);

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['purchaseOrder.items[0].bookId']").value("Book does not exist"));
    }

    @Test
    void shouldReturnBadRequestIfCouponDontExist() throws Exception {
        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "142.809.830-54",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "couponId": -1,
                    "countryId": %d,
                    "stateId": %d,
                    "phone": "123456789",
                    "postalCode": "12345",
                    "purchaseOrder": {
                        "total": 20.0,
                        "items": [
                            {
                                "bookId": %d,
                                "quantity": 1
                            }
                        ]
                    }
                }
                """.formatted(countryWithStates.getId(), state.getId(), book.getId());

        mockMvc.perform(post("/purchases").contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.couponId").value("Coupon does not exist"));
    }

    @Test
    void shouldReturnCreatedWhenNoErrors() throws Exception {
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
                    "purchaseOrder": {
                        "total": 20.0,
                        "items": [
                            {
                                "bookId": %d,
                                "quantity": 1
                            }
                        ]
                    }
                }
                """.formatted(countryWithStates.getId(), state.getId(), book.getId());

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnCreatedWithCouponWhenNoErrors() throws Exception {
        String payload = """
                {
                    "email": "john.doe@example.com",
                    "firstName": "John",
                    "lastName": "Doe",
                    "document": "142.809.830-54",
                    "address": "123 Main St",
                    "complement": "Apt 4B",
                    "city": "Springfield",
                    "couponId": %d,
                    "countryId": %d,
                    "stateId": %d,
                    "phone": "123456789",
                    "postalCode": "12345",
                    "purchaseOrder": {
                        "total": 20.0,
                        "items": [
                            {
                                "bookId": %d,
                                "quantity": 1
                            }
                        ]
                    }
                }
                """.formatted(validCoupon.getId(), countryWithStates.getId(), state.getId(), book.getId());

        Long purchaseId = Long.parseLong(mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString());

        assertNotNull(purchaseId);

        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(IllegalArgumentException::new);
        assertNotNull(purchase);

        State purchaseState = purchase.getState();
        assertNotNull(purchaseState);
        assertEquals(state, purchaseState);

        Country purchaseCountry = purchase.getCountry();
        assertNotNull(purchaseCountry);
        assertEquals(countryWithStates.getId(), purchaseCountry.getId());

        PurchaseOrder purchaseOrder = purchase.getPurchaseOrder();
        assertNotNull(purchaseOrder);
        assertEquals(1, purchaseOrder.getItems().size());
        assertEquals(new BigDecimal("20.00"), purchaseOrder.getTotal());

        PurchaseOrderItem item = purchaseOrder.getItems().get(0);
        assertNotNull(item);
        assertEquals(book, item.getBook());
        assertEquals(1, item.getQuantity());
        assertEquals(book.getPrice(), item.getPriceAtMoment());

        Coupon coupon = purchase.getCoupon();
        assertNotNull(coupon);
        assertEquals(validCoupon, coupon);
    }
}
