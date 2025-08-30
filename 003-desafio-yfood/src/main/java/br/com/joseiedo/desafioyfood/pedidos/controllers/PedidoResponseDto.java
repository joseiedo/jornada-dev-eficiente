package br.com.joseiedo.desafioyfood.pedidos.controllers;

import br.com.joseiedo.desafioyfood.pedidos.domain.Pedido;

import java.math.BigDecimal;

public record PedidoResponseDto(Long id, BigDecimal valor) {
    
    public static PedidoResponseDto from(Pedido pedido) {
        return new PedidoResponseDto(pedido.getId(), pedido.getValor());
    }
}