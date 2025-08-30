package br.com.joseiedo.desafioyfood.pagamentos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegraFraudePorEmailTest {
    
    private RegraFraudePorEmail regra;
    
    @BeforeEach
    void setUp() {
        regra = new RegraFraudePorEmail();
    }
    
    @Test
    void deveRetornarTrueParaEmailFraudador() {
        Usuario usuarioFraudador = new Usuario("fraudador@exemplo.com", Set.of(FormaPagamento.VISA));
        
        boolean resultado = regra.ehFraude(usuarioFraudador);
        
        assertTrue(resultado);
    }
    
    @Test
    void deveRetornarTrueParaEmailSuspeito() {
        Usuario usuarioSuspeito = new Usuario("suspeito@teste.com", Set.of(FormaPagamento.VISA));
        
        boolean resultado = regra.ehFraude(usuarioSuspeito);
        
        assertTrue(resultado);
    }
    
    @Test
    void deveRetornarTrueParaEmailBloqueado() {
        Usuario usuarioBloqueado = new Usuario("bloqueado@email.com", Set.of(FormaPagamento.DINHEIRO));
        
        boolean resultado = regra.ehFraude(usuarioBloqueado);
        
        assertTrue(resultado);
    }
    
    @Test
    void deveRetornarFalseParaEmailNaoFraudador() {
        Usuario usuarioNormal = new Usuario("normal@exemplo.com", Set.of(FormaPagamento.VISA));
        
        boolean resultado = regra.ehFraude(usuarioNormal);
        
        assertFalse(resultado);
    }
}