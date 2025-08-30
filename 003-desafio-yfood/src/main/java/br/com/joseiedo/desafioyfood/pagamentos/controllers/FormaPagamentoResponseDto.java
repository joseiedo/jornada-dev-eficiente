package br.com.joseiedo.desafioyfood.pagamentos.controllers;

import br.com.joseiedo.desafioyfood.pagamentos.domain.FormaPagamento;

public record FormaPagamentoResponseDto(String id, String descricao) {
    
    public FormaPagamentoResponseDto(FormaPagamento formaPagamento) {
        this(formaPagamento.name(), formaPagamento.getDescricao());
    }
}