package br.com.joseiedo.desafioyfood.pagamentos.validators;

import br.com.joseiedo.desafioyfood.pagamentos.domain.FormaPagamento;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OfflinePaymentValidator implements ConstraintValidator<ValidOfflinePayment, FormaPagamento> {
    
    @Override
    public boolean isValid(FormaPagamento formaPagamento, ConstraintValidatorContext context) {
        if (formaPagamento == null) {
            return true;
        }
        
        return !formaPagamento.getTipo().isOnline();
    }
}