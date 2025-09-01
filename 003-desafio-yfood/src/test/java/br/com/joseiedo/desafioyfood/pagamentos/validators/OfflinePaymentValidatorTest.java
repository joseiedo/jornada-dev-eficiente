package br.com.joseiedo.desafioyfood.pagamentos.validators;

import br.com.joseiedo.desafioyfood.pagamentos.domain.FormaPagamento;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class OfflinePaymentValidatorTest {
    
    private OfflinePaymentValidator validator;
    
    @Mock
    private ConstraintValidatorContext context;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new OfflinePaymentValidator();
    }
    
    @ParameterizedTest
    @EnumSource(value = FormaPagamento.class, names = {"DINHEIRO", "MAQUINA", "CHEQUE"})
    void deveRetornarTrueParaPagamentosOffline(FormaPagamento formaPagamento) {
        // When
        boolean result = validator.isValid(formaPagamento, context);
        
        // Then
        assertThat(result).isTrue();
    }
    
    @ParameterizedTest
    @EnumSource(value = FormaPagamento.class, names = {"VISA", "MASTER", "ELO"})
    void deveRetornarFalseParaPagamentosOnline(FormaPagamento formaPagamento) {
        // When
        boolean result = validator.isValid(formaPagamento, context);
        
        // Then
        assertThat(result).isFalse();
    }
    
    @Test
    void deveRetornarTrueParaFormaPagamentoNula() {
        // Given
        FormaPagamento formaPagamento = null;
        
        // When
        boolean result = validator.isValid(formaPagamento, context);
        
        // Then
        assertThat(result).isTrue();
    }
}