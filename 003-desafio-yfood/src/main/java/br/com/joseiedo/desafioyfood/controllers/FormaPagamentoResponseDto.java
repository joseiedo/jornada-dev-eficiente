package br.com.joseiedo.desafioyfood.controllers;

import br.com.joseiedo.desafioyfood.domain.FormaPagamento;

public record FormaPagamentoResponseDto(String id, String descricao) {
    
    public FormaPagamentoResponseDto(FormaPagamento formaPagamento) {
        this(formaPagamento.name(), formaPagamento.getDescricao());
    }
}