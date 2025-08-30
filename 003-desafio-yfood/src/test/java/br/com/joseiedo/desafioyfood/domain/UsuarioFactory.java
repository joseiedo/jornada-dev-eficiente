package br.com.joseiedo.desafioyfood.domain;

import java.util.Set;

public class UsuarioFactory {
    
    public static Usuario createWithFormaDePagamentoCartao() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        return new Usuario("usuario@test.com", Set.of(visa));
    }
    
    public static Usuario createWithFormaDePagamentoDinheiro() {
        FormaPagamento dinheiro = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro");
        return new Usuario("usuario@test.com", Set.of(dinheiro));
    }
    
    public static Usuario createWithMultiplasFormasPagamento() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Débito");
        FormaPagamento dinheiro = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro");
        
        Usuario usuario = new Usuario("usuario@test.com", Set.of(visa));
        usuario.adicionarFormaPagamento(master);
        usuario.adicionarFormaPagamento(dinheiro);
        return usuario;
    }
    
    public static Usuario createWithVisaEMaster() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Débito");
        
        Usuario usuario = new Usuario("usuario.visa.master@test.com", Set.of(visa));
        usuario.adicionarFormaPagamento(master);
        return usuario;
    }
    
    public static Usuario createWithCustomEmail(String email) {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Crédito");
        return new Usuario(email, Set.of(visa));
    }
    
    public static Usuario createWithSpecificFormaPagamento(FormaPagamento formaPagamento) {
        return new Usuario("usuario@test.com", Set.of(formaPagamento));
    }
}