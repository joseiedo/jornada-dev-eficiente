package br.com.joseiedo.desafioyfood.pagamentos.domain;

import java.lang.reflect.Field;
import java.util.Set;

public class UsuarioFactory {
    
    public static Usuario createWithOfflinePayments(Long id) {
        Usuario usuario = new Usuario("user.offline@test.com", 
            Set.of(FormaPagamento.DINHEIRO, FormaPagamento.MAQUINA));
        return setId(usuario, id);
    }
    
    public static Usuario createWithOnlinePayments(Long id) {
        Usuario usuario = new Usuario("user.online@test.com", 
            Set.of(FormaPagamento.VISA, FormaPagamento.MASTER, FormaPagamento.ELO));
        return setId(usuario, id);
    }
    
    public static Usuario createWithFormaPagamento(FormaPagamento formaPagamento) {
        return new Usuario("user." + formaPagamento.name().toLowerCase() + "@test.com", Set.of(formaPagamento));
    }
    
    public static Usuario createWithFormaDePagamentoCartao() {
        return createWithFormaPagamento(FormaPagamento.VISA);
    }
    
    public static Usuario createWithFormaDePagamentoDinheiro() {
        return createWithFormaPagamento(FormaPagamento.DINHEIRO);
    }
    
    public static Usuario createWithFormaDePagamentoMaquina() {
        return createWithFormaPagamento(FormaPagamento.MAQUINA);
    }
    
    public static Usuario createWithMultiplasFormasPagamento() {
        return new Usuario("user.multiplas@test.com", 
            Set.of(FormaPagamento.VISA, FormaPagamento.MASTER, FormaPagamento.DINHEIRO));
    }
    
    public static Usuario createWithVisaEMaster() {
        return new Usuario("user.visaemaster@test.com", 
            Set.of(FormaPagamento.VISA, FormaPagamento.MASTER));
    }
    
    private static Usuario setId(Usuario usuario, Long id) {
        try {
            Field field = Usuario.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(usuario, id);
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID", e);
        }
    }
}