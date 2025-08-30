package br.com.joseiedo.desafioyfood.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProcessadorRegrasFraudeTest {
    
    @Autowired
    private ProcessadorRegrasFraude processador;
    
    @Test
    void deveAplicarRegraFraudeParaEmailFraudador() {
        Usuario usuarioFraudador = UsuarioFactory.createWithCustomEmail("fraudador@exemplo.com");
        
        Set<FormaPagamento> resultado = processador.aplicarRegras(usuarioFraudador);
        
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void naoDeveAplicarRegraParaEmailNaoFraudador() {
        Usuario usuarioNormal = UsuarioFactory.createWithCustomEmail("normal@exemplo.com");
        
        Set<FormaPagamento> resultado = processador.aplicarRegras(usuarioNormal);
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(FormaPagamento.VISA));
    }
    
    @Test
    void deveRetornarVazioQuandoUsuarioFraudadorTemApenasCartao() {
        Usuario usuarioFraudador = UsuarioFactory.createWithCustomEmail("suspeito@teste.com");
        
        Set<FormaPagamento> resultado = processador.aplicarRegras(usuarioFraudador);
        
        assertTrue(resultado.isEmpty());
    }
}