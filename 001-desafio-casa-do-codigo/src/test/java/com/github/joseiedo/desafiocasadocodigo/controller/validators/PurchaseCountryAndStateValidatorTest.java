package com.github.joseiedo.desafiocasadocodigo.controller.validators;

import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

class PurchaseCountryAndStateValidatorTest {

    EntityManager entityManager = Mockito.mock(EntityManager.class);

    @Test
    void shouldReturnErrorIfCountryHasStateAndNoStateIsSent() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);
        Country country = Mockito.mock(Country.class);

        Mockito.when(request.hasState()).thenReturn(false);
        Mockito.when(country.hasStates()).thenReturn(true);
        Mockito.when(entityManager.find(Country.class, request.countryId())).thenReturn(country);

        PurchaseCountryAndStateValidator validator = new PurchaseCountryAndStateValidator(entityManager);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        Mockito.verify(entityManager).find(Country.class, request.countryId());
        Mockito.verify(country).hasStates();

        Assertions.assertTrue(errors.hasErrors());
        FieldError error = errors.getFieldError("stateId");
        Assertions.assertNotNull(error);
        Assertions.assertEquals("Country has states, state ID must be provided", error.getDefaultMessage());
    }

    @Test
    void shouldReturnErrorIfStateDoesNotBelongToCountry() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);
        Country country = Mockito.mock(Country.class);
        State state = Mockito.mock(State.class);

        Mockito.when(request.hasState()).thenReturn(true);
        Mockito.when(country.containsState(state)).thenReturn(false);
        Mockito.when(entityManager.find(Country.class, request.countryId())).thenReturn(country);
        Mockito.when(entityManager.find(State.class, request.stateId())).thenReturn(state);

        PurchaseCountryAndStateValidator validator = new PurchaseCountryAndStateValidator(entityManager);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        Mockito.verify(entityManager).find(Country.class, request.countryId());
    }

    @Test
    void shouldNotReturnErrorIfStateBelongsToCountry() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);
        Country country = Mockito.mock(Country.class);
        State state = Mockito.mock(State.class);

        Mockito.when(request.hasState()).thenReturn(true);
        Mockito.when(country.containsState(state)).thenReturn(true);
        Mockito.when(entityManager.find(Country.class, request.countryId())).thenReturn(country);
        Mockito.when(entityManager.find(State.class, request.stateId())).thenReturn(state);

        PurchaseCountryAndStateValidator validator = new PurchaseCountryAndStateValidator(entityManager);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        Mockito.verify(entityManager).find(Country.class, request.countryId());
        Mockito.verify(entityManager).find(State.class, request.stateId());

        Assertions.assertFalse(errors.hasErrors(), "No errors should be present when state belongs to country");
    }

    @Test
    void shouldNotReturnErrorIfCountryHasNoStatesAndNoStateIsSent() {
        RegisterPurchaseRequest request = Mockito.mock(RegisterPurchaseRequest.class);
        Country country = Mockito.mock(Country.class);

        Mockito.when(request.hasState()).thenReturn(false);
        Mockito.when(country.hasStates()).thenReturn(false);
        Mockito.when(entityManager.find(Country.class, request.countryId())).thenReturn(country);

        PurchaseCountryAndStateValidator validator = new PurchaseCountryAndStateValidator(entityManager);

        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        Mockito.verify(entityManager).find(Country.class, request.countryId());
    }

}