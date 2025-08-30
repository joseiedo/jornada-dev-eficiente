package br.com.joseiedo.desafioyfood.domain;

import java.util.Set;

public class RestauranteFactory {
    
    public static Restaurante createAceitandoCartoes() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Aceito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Aceito");
        return new Restaurante("Restaurante Cartões", Set.of(visa, master));
    }
    
    public static Restaurante createAceitandoDinheiro() {
        FormaPagamento dinheiro = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro Aceito");
        return new Restaurante("Restaurante Dinheiro", Set.of(dinheiro));
    }
    
    public static Restaurante createAceitandoTodasFormas() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Aceito");
        FormaPagamento master = new FormaPagamento(TipoPagamento.CARTAO_MASTER, "Master Aceito");
        FormaPagamento elo = new FormaPagamento(TipoPagamento.CARTAO_ELO, "Elo Aceito");
        FormaPagamento hypercard = new FormaPagamento(TipoPagamento.CARTAO_HYPERCARD, "Hypercard Aceito");
        FormaPagamento dinheiro = new FormaPagamento(TipoPagamento.DINHEIRO, "Dinheiro Aceito");
        
        return new Restaurante("Restaurante Completo", Set.of(visa, master, elo, hypercard, dinheiro));
    }
    
    public static Restaurante createAceitandoApenasVisa() {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Aceito");
        return new Restaurante("Restaurante Visa", Set.of(visa));
    }
    
    public static Restaurante createAceitandoEloEHypercard() {
        FormaPagamento elo = new FormaPagamento(TipoPagamento.CARTAO_ELO, "Elo Aceito");
        FormaPagamento hypercard = new FormaPagamento(TipoPagamento.CARTAO_HYPERCARD, "Hypercard Aceito");
        return new Restaurante("Restaurante Elo/Hypercard", Set.of(elo, hypercard));
    }
    
    public static Restaurante createWithCustomName(String nome) {
        FormaPagamento visa = new FormaPagamento(TipoPagamento.CARTAO_VISA, "Visa Aceito");
        return new Restaurante(nome, Set.of(visa));
    }
    
    public static Restaurante createWithSpecificFormasPagamento(Set<FormaPagamento> formasPagamento) {
        return new Restaurante("Restaurante Customizado", formasPagamento);
    }
}