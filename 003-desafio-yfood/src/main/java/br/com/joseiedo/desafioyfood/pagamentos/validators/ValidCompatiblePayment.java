package br.com.joseiedo.desafioyfood.pagamentos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CompatiblePaymentValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCompatiblePayment {
    String message() default "Combinacao de restaurante + usuario nao aceita a forma de pagamento selecionada";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}