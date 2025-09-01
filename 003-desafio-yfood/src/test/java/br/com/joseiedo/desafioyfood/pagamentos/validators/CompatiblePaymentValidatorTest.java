package br.com.joseiedo.desafioyfood.pagamentos.validators;

import br.com.joseiedo.desafioyfood.pagamentos.controllers.TransacaoRequestDto;
import br.com.joseiedo.desafioyfood.pagamentos.domain.*;
import br.com.joseiedo.desafioyfood.pagamentos.repository.RestauranteRepository;
import br.com.joseiedo.desafioyfood.pagamentos.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CompatiblePaymentValidatorTest {
    
    private CompatiblePaymentValidator validator;
    
    @Mock
    private RestauranteRepository restauranteRepository;
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private ConstraintValidatorContext context;
    
    @BeforeEach
    void setUp() {
        Set<RegraFraude> regras = Set.of();
        validator = new CompatiblePaymentValidator(restauranteRepository, usuarioRepository, regras);
        setupMockData();
    }
    
    private void setupMockData() {
        when(restauranteRepository.findById(1L))
            .thenReturn(Optional.of(RestauranteFactory.createWithOfflinePayments(1L)));
        when(restauranteRepository.findById(2L))
            .thenReturn(Optional.of(RestauranteFactory.createWithOnlinePayments(2L)));
        
        when(usuarioRepository.findById(1L))
            .thenReturn(Optional.of(UsuarioFactory.createWithOfflinePayments(1L)));
        when(usuarioRepository.findById(2L))
            .thenReturn(Optional.of(UsuarioFactory.createWithOnlinePayments(2L)));
        
        when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
    }
    
    @ParameterizedTest
    @MethodSource("compatiblePaymentScenarios")
    void deveRetornarTrueParaCombinacaoCompativel(Long restauranteId, Long usuarioId, FormaPagamento formaPagamento) {
        // Given
        TransacaoRequestDto request = createTransacaoRequest(restauranteId, usuarioId, formaPagamento);
        
        // When
        boolean result = validator.isValid(request, context);
        
        // Then
        assertThat(result).isTrue();
    }
    
    @ParameterizedTest
    @MethodSource("incompatiblePaymentScenarios")
    void deveRetornarFalseParaCombinacaoIncompativel(Long restauranteId, Long usuarioId, FormaPagamento formaPagamento) {
        // Given
        TransacaoRequestDto request = createTransacaoRequest(restauranteId, usuarioId, formaPagamento);
        
        // When
        boolean result = validator.isValid(request, context);
        
        // Then
        assertThat(result).isFalse();
    }
    
    @ParameterizedTest
    @MethodSource("nullFieldScenarios")
    void deveRetornarTrueParaCamposNulos(Long restauranteId, Long usuarioId, FormaPagamento formaPagamento) {
        // Given
        TransacaoRequestDto request = createTransacaoRequest(restauranteId, usuarioId, formaPagamento);
        
        // When
        boolean result = validator.isValid(request, context);
        
        // Then
        assertThat(result).isTrue();
    }
    
    @ParameterizedTest
    @MethodSource("nonExistentEntityScenarios")
    void deveRetornarTrueParaEntidadesInexistentes(Long restauranteId, Long usuarioId) {
        // Given
        TransacaoRequestDto request = createTransacaoRequest(restauranteId, usuarioId, FormaPagamento.DINHEIRO);
        
        // When
        boolean result = validator.isValid(request, context);
        
        // Then
        assertThat(result).isTrue();
    }
    
    private TransacaoRequestDto createTransacaoRequest(Long restauranteId, Long usuarioId, FormaPagamento formaPagamento) {
        return new TransacaoRequestDto(
            1L, restauranteId, usuarioId, BigDecimal.valueOf(50.00), formaPagamento, null
        );
    }
    
    static Stream<Arguments> compatiblePaymentScenarios() {
        return Stream.of(
            // Restaurante 1 (DINHEIRO, MAQUINA, CHEQUE) + Usuario 1 (DINHEIRO, MAQUINA)
            Arguments.of(1L, 1L, FormaPagamento.DINHEIRO),
            Arguments.of(1L, 1L, FormaPagamento.MAQUINA),
            
            // Restaurante 2 (VISA, MASTER) + Usuario 2 (VISA, MASTER, ELO)
            Arguments.of(2L, 2L, FormaPagamento.VISA),
            Arguments.of(2L, 2L, FormaPagamento.MASTER)
        );
    }
    
    static Stream<Arguments> incompatiblePaymentScenarios() {
        return Stream.of(
            // Restaurante 1 (offline only) + Usuario 1 (offline only) + online payment
            Arguments.of(1L, 1L, FormaPagamento.VISA),
            Arguments.of(1L, 1L, FormaPagamento.MASTER),
            Arguments.of(1L, 1L, FormaPagamento.ELO),
            
            // Restaurante 2 (online only) + Usuario 2 (online only) + offline payment
            Arguments.of(2L, 2L, FormaPagamento.DINHEIRO),
            Arguments.of(2L, 2L, FormaPagamento.MAQUINA),
            Arguments.of(2L, 2L, FormaPagamento.CHEQUE),
            
            // Cross combinations that don't match
            Arguments.of(1L, 2L, FormaPagamento.VISA),
            Arguments.of(2L, 1L, FormaPagamento.DINHEIRO),
            
            // Usuario 1 doesn't have CHEQUE
            Arguments.of(1L, 1L, FormaPagamento.CHEQUE),
            
            // Usuario 2 has ELO but restaurant 2 doesn't accept it
            Arguments.of(2L, 2L, FormaPagamento.ELO)
        );
    }
    
    static Stream<Arguments> nullFieldScenarios() {
        return Stream.of(
            Arguments.of(null, 1L, FormaPagamento.DINHEIRO),
            Arguments.of(1L, null, FormaPagamento.DINHEIRO),
            Arguments.of(1L, 1L, null)
        );
    }
    
    static Stream<Arguments> nonExistentEntityScenarios() {
        return Stream.of(
            Arguments.of(999L, 1L),
            Arguments.of(1L, 999L)
        );
    }
}