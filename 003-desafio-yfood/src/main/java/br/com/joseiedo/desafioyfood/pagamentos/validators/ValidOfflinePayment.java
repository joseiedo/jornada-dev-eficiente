package br.com.joseiedo.desafioyfood.pagamentos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = OfflinePaymentValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOfflinePayment {
    String message() default "Forma de pagamento deve ser offline";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}