package com.github.joseiedo.desafiocasadocodigo.controller.validators;

import com.github.joseiedo.desafiocasadocodigo.dto.purchases.RegisterPurchaseRequest;
import com.github.joseiedo.desafiocasadocodigo.model.country.Country;
import com.github.joseiedo.desafiocasadocodigo.model.state.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@SuppressWarnings("unchecked")
@Component
public class PurchaseCountryAndStateValidator implements Validator {

    @PersistenceContext
    private EntityManager entityManager;

    public PurchaseCountryAndStateValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RegisterPurchaseRequest.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        if (errors.hasErrors()) return;
        RegisterPurchaseRequest request = (RegisterPurchaseRequest) target;
        Assert.notNull(request.countryId(), "Country ID must not be null");

        Country country = entityManager.find(Country.class, request.countryId());

        if (request.hasState()) {
            State state = entityManager.find(State.class, request.stateId());
            if (!country.containsState(state)) {
                errors.rejectValue("stateId", "stateId", "State ID does not belong to the given country");
            }
        } else if (country.hasStates()) {
            errors.rejectValue("stateId", "stateId", "Country has states, state ID must be provided");
        }
    }
}
