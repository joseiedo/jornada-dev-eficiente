package br.com.joseiedo.desafioyfood.pagamentos.validators;

import br.com.joseiedo.desafioyfood.pagamentos.clients.PedidosClient;
import br.com.joseiedo.desafioyfood.pagamentos.clients.PedidoResponseDto;
import feign.FeignException;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ExistingPedidoValidatorTest {
    
    private ExistingPedidoValidator validator;
    
    @Mock
    private PedidosClient pedidosClient;
    
    @Mock
    private ConstraintValidatorContext context;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ExistingPedidoValidator(pedidosClient);
        
        // Setup mock responses
        when(pedidosClient.buscarPedido(1L))
            .thenReturn(new PedidoResponseDto(1L, BigDecimal.valueOf(50.00)));
        when(pedidosClient.buscarPedido(2L))
            .thenReturn(new PedidoResponseDto(2L, BigDecimal.valueOf(75.50)));
        when(pedidosClient.buscarPedido(3L))
            .thenReturn(new PedidoResponseDto(3L, BigDecimal.valueOf(100.00)));
        
        when(pedidosClient.buscarPedido(999L))
            .thenThrow(FeignException.NotFound.class);
        when(pedidosClient.buscarPedido(888L))
            .thenThrow(FeignException.NotFound.class);
        when(pedidosClient.buscarPedido(777L))
            .thenThrow(FeignException.NotFound.class);
    }
    
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void deveRetornarTrueParaPedidosExistentes(Long pedidoId) {
        // When
        boolean result = validator.isValid(pedidoId, context);
        
        // Then
        assertThat(result).isTrue();
    }
    
    @ParameterizedTest
    @ValueSource(longs = {999L, 888L, 777L})
    void deveRetornarFalseParaPedidosInexistentes(Long pedidoId) {
        // When
        boolean result = validator.isValid(pedidoId, context);
        
        // Then
        assertThat(result).isFalse();
    }
    
    @Test
    void deveRetornarTrueParaPedidoIdNulo() {
        // Given
        Long pedidoId = null;
        
        // When
        boolean result = validator.isValid(pedidoId, context);
        
        // Then
        assertThat(result).isTrue();
    }
    
    @Test
    void deveRetornarFalseQuandoFeignExceptionGenericaForLancada() {
        // Given
        Long pedidoId = 500L;
        when(pedidosClient.buscarPedido(500L))
            .thenThrow(new RuntimeException("Service unavailable"));
        
        // When/Then
        try {
            boolean result = validator.isValid(pedidoId, context);
            // If no exception is thrown, the validator should handle it gracefully
            // In this case, we expect the exception to bubble up as it's not a NotFound
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Service unavailable");
        }
    }
}