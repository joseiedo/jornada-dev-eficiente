package br.com.joseiedo.desafioyfood.domain;

import java.util.Set;

public class UsuarioFactory {
    
    public static Usuario createWithFormaDePagamentoCartao() {
        FormaPagamento visa = FormaPagamento.VISA;
        return new Usuario("usuario@test.com", Set.of(visa));
    }
    
    public static Usuario createWithFormaDePagamentoDinheiro() {
        FormaPagamento dinheiro = FormaPagamento.DINHEIRO;
        return new Usuario("usuario@test.com", Set.of(dinheiro));
    }
    
    public static Usuario createWithMultiplasFormasPagamento() {
        FormaPagamento visa = FormaPagamento.VISA;
        FormaPagamento master = FormaPagamento.MASTER;
        
        return new Usuario("usuario@test.com", Set.of(visa, master));
    }
    
    public static Usuario createWithVisaEMaster() {
        FormaPagamento visa = FormaPagamento.VISA;
        FormaPagamento master = FormaPagamento.MASTER;
        
        return new Usuario("usuario.visa.master@test.com", Set.of(visa, master));
    }
    
    public static Usuario createWithCustomEmail(String email) {
        FormaPagamento visa = FormaPagamento.VISA;
        return new Usuario(email, Set.of(visa));
    }
    
    public static Usuario createWithSpecificFormaPagamento(FormaPagamento formaPagamento) {
        return new Usuario("usuario@test.com", Set.of(formaPagamento));
    }
}