package br.com.joseiedo.desafioyfood.domain;

import java.util.Set;

public class RestauranteFactory {
    
    public static Restaurante createAceitandoCartoes() {
        FormaPagamento visa = FormaPagamento.VISA;
        FormaPagamento master = FormaPagamento.MASTER;
        return new Restaurante("Restaurante Cartoes", Set.of(visa, master));
    }
    
    public static Restaurante createAceitandoDinheiro() {
        FormaPagamento dinheiro = FormaPagamento.DINHEIRO;
        return new Restaurante("Restaurante Dinheiro", Set.of(dinheiro));
    }
    
    public static Restaurante createAceitandoTodasFormas() {
        FormaPagamento visa = FormaPagamento.VISA;
        FormaPagamento master = FormaPagamento.MASTER;
        FormaPagamento elo = FormaPagamento.ELO;
        FormaPagamento dinheiro = FormaPagamento.DINHEIRO;
        FormaPagamento maquina = FormaPagamento.MAQUINA;
        FormaPagamento cheque = FormaPagamento.CHEQUE;
        
        return new Restaurante("Restaurante Completo", Set.of(visa, master, elo, dinheiro, maquina, cheque));
    }
    
    public static Restaurante createAceitandoApenasVisa() {
        FormaPagamento visa = FormaPagamento.VISA;
        return new Restaurante("Restaurante Visa", Set.of(visa));
    }
    
    public static Restaurante createAceitandoEloEMaquina() {
        FormaPagamento elo = FormaPagamento.ELO;
        FormaPagamento maquina = FormaPagamento.MAQUINA;
        return new Restaurante("Restaurante Elo/Maquina", Set.of(elo, maquina));
    }
    
    public static Restaurante createWithCustomName(String nome) {
        FormaPagamento visa = FormaPagamento.VISA;
        return new Restaurante(nome, Set.of(visa));
    }
    
    public static Restaurante createWithSpecificFormasPagamento(Set<FormaPagamento> formasPagamento) {
        return new Restaurante("Restaurante Customizado", formasPagamento);
    }
}